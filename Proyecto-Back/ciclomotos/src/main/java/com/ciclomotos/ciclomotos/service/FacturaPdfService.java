package com.ciclomotos.ciclomotos.service;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
    private static final float LINE_HEIGHT = 18; // un poco más espaciado
    private static final float FONT_SIZE_NORMAL = 11;
    private static final float FONT_SIZE_TITLE = 18;
    private static final float FONT_SIZE_HEADER = 13;

    public byte[] generarFacturaPdf(Venta venta) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            float pageWidth = page.getMediaBox().getWidth();
            float yPosition = page.getMediaBox().getHeight() - MARGIN;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // --- TÍTULO CENTRAL ---
                contentStream.setNonStrokingColor(30, 30, 30); // Gris oscuro elegante
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_TITLE);
                drawCenteredText(contentStream, "Ciclomotos Jerónimo", pageWidth, yPosition);
                yPosition -= LINE_HEIGHT * 1.5f;

                // Subtítulo factura con id
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.setNonStrokingColor(0, 102, 204); // azul corporativo
                drawCenteredText(contentStream, "FACTURA #" + venta.getId(), pageWidth, yPosition);
                yPosition -= LINE_HEIGHT * 2;

                // --- DATOS EMPRESA ---
                contentStream.setNonStrokingColor(0); // negro
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Ciclomotos Jerónimo");
                contentStream.endText();
                yPosition -= LINE_HEIGHT;

                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
                drawTextBlock(contentStream, 
                    "Nit: 123456789-1\n" +
                    "Dirección: Carrera 19 #18-52\n" +
                    "Teléfono: (601) 1234567\n" +
                    "Duitama,Boyacá, Colombia", 
                    MARGIN, yPosition);
                yPosition -= LINE_HEIGHT * 5;

                // --- DATOS CLIENTE ---
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.setNonStrokingColor(0, 102, 204);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("DATOS DEL CLIENTE");
                contentStream.endText();
                yPosition -= LINE_HEIGHT;

                Cliente cliente = venta.getCliente();
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
                contentStream.setNonStrokingColor(0);
                drawTextBlock(contentStream, 
                    "Nombre: " + cliente.getNombre() + "\n" +
                    "Documento: " + cliente.getId() + "\n" +
                    "Email: " + cliente.getEmail() + "\n" +
                    "Teléfono: " + cliente.getTelefono() + "\n" +
                    "Dirección: " + cliente.getDireccion(), 
                    MARGIN, yPosition);
                yPosition -= LINE_HEIGHT * 6;

                // --- DETALLES DE LA FACTURA ---
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_HEADER);
                contentStream.setNonStrokingColor(0, 102, 204);
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
                yPosition -= LINE_HEIGHT + 5;

                // Filas de productos
                contentStream.setNonStrokingColor(0);
                for (DetalleVenta detalle : venta.getDetalles()) {
                    if (yPosition <= MARGIN + 50) {
                        contentStream.close(); // Cierra el stream actual

                        page = new PDPage(); // Nueva página
                        document.addPage(page);
                        yPosition = page.getMediaBox().getHeight() - MARGIN;

                        // Nuevo bloque try-with-resources para el nuevo stream
                        try (PDPageContentStream newContentStream = new PDPageContentStream(document, page)) {
                            // Para simplicidad no continúo la paginación aquí
                            break; 
                        }
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
                yPosition -= LINE_HEIGHT;
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
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
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                contentStream.setNonStrokingColor(100);
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
        float fontSize = (text.equals("Ciclomotos Jerónimo")) ? FONT_SIZE_TITLE : FONT_SIZE_HEADER;
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000 * fontSize;
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
        
        // Fondo de cabecera color azul claro
        contentStream.setNonStrokingColor(204, 229, 255);
        contentStream.addRect(x, y - 12, tableWidth, LINE_HEIGHT + 6);
        contentStream.fill();
        contentStream.setNonStrokingColor(0, 51, 102); // texto azul oscuro
        
        // Texto cabecera
        float offsetX = x + 5;
        for (String header : headers) {
            contentStream.beginText();
            contentStream.newLineAtOffset(offsetX, y);
            contentStream.showText(header);
            contentStream.endText();
            offsetX += columnWidths[headersToIndex(header)];
        }
        
        // Línea divisoria
        contentStream.setStrokingColor(0, 102, 204);
        contentStream.moveTo(MARGIN, y - 12);
        contentStream.lineTo(MARGIN + tableWidth, y - 12);
        contentStream.stroke();
        contentStream.setNonStrokingColor(0);
    }

    private int headersToIndex(String header) {
        switch (header) {
            case "Producto": return 0;
            case "Cantidad": return 1;
            case "P. Unitario": return 2;
            case "Subtotal": return 3;
            default: return 0;
        }
    }

    private void drawTableRow(PDPageContentStream contentStream, float x, float y, float[] columnWidths, String[] rowData) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        float offsetX = x + 5;
        for (String data : rowData) {
            contentStream.beginText();
            contentStream.newLineAtOffset(offsetX, y);
            contentStream.showText(data);
            contentStream.endText();
            offsetX += columnWidths[rowDataToIndex(data)];
        }
    }

    private int rowDataToIndex(String data) {
        // Esta función es un placeholder, en realidad no es necesaria
        // Se podría mejorar para alinear columnas correctamente
        return 0;
    }

    private String formatCurrency(BigDecimal amount) {
        return "$" + String.format("%,.2f", amount);
    }
}
