package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.model.DetalleVenta;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
public class FacturaPdfService {

    public byte[] generarFacturaPdf(Venta venta) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Configuración inicial
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart;
                float lineHeight = 15;

                // Encabezado
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("FACTURA #" + venta.getId());
                contentStream.endText();
                yPosition -= lineHeight * 2;

                // Datos del cliente
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Cliente: " + venta.getCliente().getNombre());
                contentStream.endText();
                yPosition -= lineHeight;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Fecha: " + venta.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE));
                contentStream.endText();
                yPosition -= lineHeight * 2;

                // Tabla de productos
                String[] headers = {"Producto", "Cantidad", "P. Unitario", "Subtotal"};
                float[] columnWidths = {200, 80, 100, 100};
                float tableTopY = yPosition;

                // Dibujar encabezados de tabla
                drawTableHeader(contentStream, margin, tableTopY, columnWidths, headers);
                yPosition -= lineHeight;

                // Detalles de productos
                for (DetalleVenta detalle : venta.getDetalles()) {
                    String[] rowData = {
                        detalle.getProducto().getNombre(),
                        detalle.getCantidad().toString(),
                        "$" + detalle.getPrecioUnitario().toString(),
                        "$" + detalle.getSubtotal().toString()
                    };
                    drawTableRow(contentStream, margin, yPosition, columnWidths, rowData);
                    yPosition -= lineHeight;
                }

                // Totales
                yPosition -= lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 200, yPosition);
                contentStream.showText("Subtotal: $" + venta.getSubtotal());
                contentStream.endText();
                yPosition -= lineHeight;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 200, yPosition);
                contentStream.showText("IVA (19%): $" + venta.getIva());
                contentStream.endText();
                yPosition -= lineHeight;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin + 200, yPosition);
                contentStream.showText("TOTAL: $" + venta.getTotal());
                contentStream.endText();
            }

            // Guardar PDF en byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private void drawTableHeader(PDPageContentStream contentStream, float x, float y, float[] columnWidths, String[] headers) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(headers[i]);
            contentStream.endText();
            x += columnWidths[i];
        }
    }

    private void drawTableRow(PDPageContentStream contentStream, float x, float y, float[] columnWidths, String[] rowData) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (int i = 0; i < rowData.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(rowData[i]);
            contentStream.endText();
            x += columnWidths[i];
        }
    }
}