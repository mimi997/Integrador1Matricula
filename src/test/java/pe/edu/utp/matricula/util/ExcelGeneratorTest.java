package pe.edu.utp.matricula.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelGeneratorTest {

    @Test
    public void testGenerarReporte() throws IOException {
        ExcelGenerator generator = new ExcelGenerator();
        String[] headers = {"Id", "Nombre", "Email"};
        List<String[]> data = List.of(
                new String[]{"1", "Juan", "juan@utp.edu.pe"},
                new String[]{"2", "Maria", "maria@utp.edu.pe"}
        );

        byte[] result = generator.generarReporte("TestSheet", headers, data);
        assertThat(result).isNotNull();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("TestSheet");
            assertThat(sheet).isNotNull();

            // Verificar número de filas (1 cabecera + 2 de datos)
            assertThat(sheet.getPhysicalNumberOfRows()).isEqualTo(3);

            // Cabecera
            Row headerRow = sheet.getRow(0);
            assertThat(headerRow.getCell(0).getStringCellValue()).isEqualTo("Id");
            assertThat(headerRow.getCell(1).getStringCellValue()).isEqualTo("Nombre");
            assertThat(headerRow.getCell(2).getStringCellValue()).isEqualTo("Email");

            // Datos
            Row row1 = sheet.getRow(1);
            assertThat(row1.getCell(0).getStringCellValue()).isEqualTo("1");
            assertThat(row1.getCell(1).getStringCellValue()).isEqualTo("Juan");

            Row row2 = sheet.getRow(2);
            assertThat(row2.getCell(0).getStringCellValue()).isEqualTo("2");
            assertThat(row2.getCell(1).getStringCellValue()).isEqualTo("Maria");
        }
    }
}
