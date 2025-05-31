package com.ciclomotos.ciclomotos.service;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.ciclomotos.ciclomotos.model.Cliente;
import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.model.Venta;

@Service
public class FacturaPdfService {
    
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 15;
    private static final float FONT_SIZE_NORMAL = 10;
    private static final float FONT_SIZE_TITLE = 14;
    private static final float FONT_SIZE_HEADER = 12;

    public byte[] generarFacturaPdf(Venta venta) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            float pageWidth = page.getMediaBox().getWidth();
            float yPosition = page.getMediaBox().getHeight() - MARGIN;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // --- ENCABEZADO ---
                // Logo (opcional)
                // PDImageXObject logo = PDImageXObject.createFromFile("ruta/logo.png", document);
                // contentStream.drawImage(logo, MARGIN, yPosition - 50, 100, 50);

                // Título
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_TITLE);
                drawCenteredText(contentStream, "FACTURA #" + venta.getId(), pageWidth, yPosition);
                yPosition -= LINE_HEIGHT * 2;

                // --- DATOS EMPRESA ---
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("CICLOMOTOS S.A.S");
                contentStream.endText();
                yPosition -= LINE_HEIGHT;

                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
                drawTextBlock(contentStream, 
                    "Nit: 123456789-1\n" +
                    "Dirección: Calle 123 #45-67\n" +
                    "Teléfono: (601) 1234567\n" +
                    "Bogotá D.C., Colombia", 
                    MARGIN, yPosition);
                yPosition -= LINE_HEIGHT * 5;

                // --- DATOS CLIENTE ---
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("DATOS DEL CLIENTE");
                contentStream.endText();
                yPosition -= LINE_HEIGHT;

                Cliente cliente = venta.getCliente();
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
                drawTextBlock(contentStream, 
                    "Nombre: " + cliente.getNombre() + "\n" +
                    "Documento: " + cliente.getId() + "\n" +
                    "Email: " + cliente.getEmail() + "\n" +
                    "Teléfono: " + cliente.getTelefono() + "\n" +
                    "Dirección: " + cliente.getDireccion(), 
                    MARGIN, yPosition);
                yPosition -= LINE_HEIGHT * 6;

                // --- DETALLES FACTURA ---
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("DETALLES DE LA FACTURA");
                contentStream.endText();
                yPosition -= LINE_HEIGHT;

                // Tabla de productos
                float tableWidth = pageWidth - 2 * MARGIN;
                float[] columnWidths = {200f, 80f, 100f, 100f};
                String[] headers = {"Producto", "Cantidad", "P. Unitario", "Subtotal"};

                // Cabecera de tabla
                drawTableHeader(contentStream, MARGIN, yPosition, columnWidths, headers, tableWidth);
                yPosition -= LINE_HEIGHT;

                // Filas de productos
                for (DetalleVenta detalle : venta.getDetalles()) {
                    if (yPosition <= MARGIN) {
                        contentStream.close(); // Cierra el stream actual

                        page = new PDPage(); // Nueva página
                        document.addPage(page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;

                        // Nuevo bloque try-with-resources para el nuevo stream
                        try (PDPageContentStream newContentStream = new PDPageContentStream(document, page)) {
                            // ...dibuja encabezados de tabla si es necesario...
                            // ...continúa dibujando detalles...
                        }
                        break; // O ajusta la lógica para seguir iterando
                    }

                    String[] rowData = {
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad().toString(),
                        formatCurrency(detalle.getPrecioUnitario()),
                        formatCurrency(detalle.getSubtotal())
                    };
                    drawTableRow(contentStream, MARGIN, yPosition, columnWidths, rowData);
                    yPosition -= LINE_HEIGHT;
                }

                // --- TOTALES ---
                yPosition -= LINE_HEIGHT * 2;
                drawRightAlignedText(contentStream, "Subtotal: " + formatCurrency(venta.getSubtotal()), 
                    pageWidth - MARGIN, yPosition);
                yPosition -= LINE_HEIGHT;
                drawRightAlignedText(contentStream, "IVA (19%): " + formatCurrency(venta.getIva()), 
                    pageWidth - MARGIN, yPosition);
                yPosition -= LINE_HEIGHT;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_NORMAL);
                drawRightAlignedText(contentStream, "TOTAL: " + formatCurrency(venta.getTotal()), 
                    pageWidth - MARGIN, yPosition);

                // --- PIE DE PÁGINA ---
                yPosition -= LINE_HEIGHT * 3;
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);
                drawCenteredText(contentStream, "¡Gracias por su compra!", pageWidth, yPosition);
                yPosition -= LINE_HEIGHT;
                drawCenteredText(contentStream, "Factura generada el: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE), 
                    pageWidth, yPosition);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    // --- Métodos auxiliares ---
    private void drawTextBlock(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        String[] lines = text.split("\n");
        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(line);
            contentStream.endText();
            y -= LINE_HEIGHT;
        }
    }

    private void drawCenteredText(PDPageContentStream contentStream, String text, float pageWidth, float y) throws IOException {
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000 * FONT_SIZE_TITLE;
        float x = (pageWidth - textWidth) / 2;
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void drawRightAlignedText(PDPageContentStream contentStream, String text, float rightMargin, float y) throws IOException {
        float textWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000 * FONT_SIZE_NORMAL;
        contentStream.beginText();
        contentStream.newLineAtOffset(rightMargin - textWidth, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void drawTableHeader(PDPageContentStream contentStream, float x, float y, float[] columnWidths, String[] headers, float tableWidth) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_NORMAL);
        contentStream.setLineWidth(1f);
        
        // Dibujar fondo de cabecera
        contentStream.setNonStrokingColor(200, 200, 200);
        contentStream.addRect(x, y - 10, tableWidth, LINE_HEIGHT + 5);
        contentStream.fill();
        contentStream.setNonStrokingColor(0, 0, 0);
        
        // Texto de cabecera
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + 5, y);
            contentStream.showText(headers[i]);
            contentStream.endText();
            x += columnWidths[i];
        }
        
        // Línea divisoria
        contentStream.moveTo(MARGIN, y - 12);
        contentStream.lineTo(MARGIN + tableWidth, y - 12);
        contentStream.stroke();
    }

    private void drawTableRow(PDPageContentStream contentStream, float x, float y, float[] columnWidths, String[] rowData) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + 5, y);
            contentStream.showText(rowData[i]);
            contentStream.endText();
            x += columnWidths[i];
        }
    }

    private String formatCurrency(BigDecimal amount) {
        return "$" + String.format("%,.2f", amount);
    }
}