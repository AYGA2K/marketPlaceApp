package handler

import (
	"errors"
	"server/internal/database"
	"server/internal/model"

	"github.com/gofiber/fiber/v2"
	"gorm.io/gorm"
)

type ProductHandler struct {
	DBService database.Service
}

func NewProductHandler(dbService database.Service) *ProductHandler {
	return &ProductHandler{DBService: dbService}
}

func (h *ProductHandler) CreateProduct(c *fiber.Ctx) error {
	var product model.Product
	if err := c.BodyParser(&product); err != nil {
		return c.Status(400).JSON(err.Error())
	}

	res := h.DBService.GetDB().Create(&product)
	if res.Error != nil {
		return c.Status(500).JSON(res.Error.Error())
	} else {
		return c.Status(200).JSON(product)
	}
}

func (h *ProductHandler) GetProducts(c *fiber.Ctx) error {
	db := h.DBService.GetDB()
	products := []model.Product{}
	db.Find(&products)

	return c.Status(200).JSON(products)
}

func (h *ProductHandler) GetAvailableProducts(c *fiber.Ctx) error {
	db := h.DBService.GetDB()
	availableProducts := []model.Product{}
	db.Find(&availableProducts, "is_available = ?", true)

	return c.Status(200).JSON(availableProducts)
}

func (h *ProductHandler) GetProduct(c *fiber.Ctx) error {
	db := h.DBService.GetDB()
	id, err := c.ParamsInt("id")
	var product model.Product
	if err != nil {
		return c.Status(400).JSON("Please ensure that :id is an integer")
	}

	if err := findProduct(id, db, &product); err != nil {
		return c.Status(400).JSON(err.Error())
	}

	return c.Status(200).JSON(product)
}

func (h *ProductHandler) UpdateProduct(c *fiber.Ctx) error {
	db := h.DBService.GetDB()
	id, err := c.ParamsInt("id")

	var product model.Product

	if err != nil {
		return c.Status(400).JSON("Please ensure that :id is an integer")
	}

	err = findProduct(id, db, &product)
	if err != nil {
		return c.Status(400).JSON(err.Error())
	}

	type UpdateProduct struct {
		Desc        string  `json:"desc"`
		Image       string  `json:"image"`
		Price       float64 `json:"price"`
		IsAvailable bool    `json:"is_available"`
	}

	var updateData UpdateProduct

	if err := c.BodyParser(&updateData); err != nil {
		return c.Status(500).JSON(err.Error())
	}

	product.Desc = updateData.Desc
	product.Image = updateData.Image
	product.Price = updateData.Price
	product.IsAvailable = updateData.IsAvailable

	db.Save(&product)

	return c.Status(200).JSON(product)
}

func (h *ProductHandler) ToggleProductAvailability(c *fiber.Ctx) error {
	db := h.DBService.GetDB()
	id, err := c.ParamsInt("id")

	var product model.Product

	if err != nil {
		return c.Status(400).JSON("Please ensure that :id is an integer")
	}

	err = findProduct(id, db, &product)
	if err != nil {
		return c.Status(400).JSON(err.Error())
	}

	// Toggle the IsAvailable field
	product.IsAvailable = !product.IsAvailable

	db.Save(&product)

	return c.Status(200).JSON(product)
}

func (h *ProductHandler) DeleteProduct(c *fiber.Ctx) error {
	db := h.DBService.GetDB()
	id, err := c.ParamsInt("id")

	var product model.Product

	if err != nil {
		return c.Status(400).JSON("Please ensure that :id is an integer")
	}

	err = findProduct(id, db, &product)

	if err != nil {
		return c.Status(400).JSON(err.Error())
	}

	if err = db.Delete(&product).Error; err != nil {
		return c.Status(404).JSON(err.Error())
	}

	return c.Status(200).JSON("Successfully deleted Product")
}

func findProduct(id int, db *gorm.DB, product *model.Product) error {
	db.Find(&product, "id = ?", id)
	if product.ID == 0 {
		return errors.New("product does not exist")
	}
	return nil
}
