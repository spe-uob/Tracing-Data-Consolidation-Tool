package Group1.com.DataConsolidation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private HelloController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void apachePoi() {
		Workbook wb = new XSSFWorkbook();
		assertThat(wb).isNotNull();

		Sheet sheet = wb.createSheet("foo");
		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("bar");
		assertThat(cell.getStringCellValue()).isEqualTo("bar");
	}
}
