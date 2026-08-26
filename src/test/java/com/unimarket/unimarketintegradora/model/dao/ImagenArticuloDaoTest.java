package com.unimarket.unimarketintegradora.model.dao;

import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImagenArticuloDaoTest {
    private ImagenArticuloDao dao;
    private ImagenArticulo imagenPrueba;
    private final int ID_ARTICULO_TEST = 1;
    private final int ID_IMAGEN_TEST = 1;

    @BeforeEach
    void setUp() {
        this.dao = new ImagenArticuloDao();
        this.imagenPrueba = new ImagenArticulo(1, "https://ejemplo.com/imagen.jpg");
        this.imagenPrueba.setIdImagen(1);
    }

    @Test
    void create() {
        Assertions.assertDoesNotThrow(() -> this.dao.create(this.imagenPrueba), "La creación de la imagen del artículo no debería lanzar excepciones SQL.");
    }

    @Test
    void getAll() {
        List<ImagenArticulo> lista = this.dao.getAll();
        Assertions.assertNotNull(lista, "La lista devuelta por getAll no debe ser nula.");
    }

    @Test
    void getById() {
        Assertions.assertDoesNotThrow(() -> this.dao.getById(1), "La consulta de la imagen por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void getByArticuloId() {
        List<ImagenArticulo> lista = this.dao.getByArticuloId(1);
        Assertions.assertNotNull(lista, "La lista de imágenes por ID de artículo no debe ser nula.");
    }

    @Test
    void update() {
        boolean resultado = this.dao.update(this.imagenPrueba);
        Assertions.assertFalse(resultado, "El método update debe retornar false ya que no está implementado para imágenes.");
    }

    @Test
    void delete() {
        Assertions.assertDoesNotThrow(() -> this.dao.delete(1), "La eliminación de la imagen por ID no debería lanzar excepciones SQL.");
    }

    @Test
    void obtenerPorArticulo() {
        List<ImagenArticulo> lista = this.dao.obtenerPorArticulo(1);
        Assertions.assertNotNull(lista, "La obtención de imágenes por artículo no debe retornar nulo.");
    }

    @Test
    void eliminarPorArticulo() {
        Assertions.assertDoesNotThrow(() -> this.dao.eliminarPorArticulo(1), "La eliminación de imágenes por artículo no debería lanzar excepciones SQL.");
    }
}