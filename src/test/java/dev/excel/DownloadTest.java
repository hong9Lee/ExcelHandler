package dev.excel;

import dev.excel.dto.SampleVO;
import dev.excel.repository.JdbcRepository;
import dev.excel.utils.handler.SheetExcelFile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dev.excel.utils.DataUtils.getUploadDirectoryPath;

@SpringBootTest
public class DownloadTest {

    @Autowired
    private JdbcRepository jdbcRepository;

    @Test
    @Description("엑셀 다운로드 테스트")
    @Transactional
    public void excelDownloadTest() throws IOException {
        SheetExcelFile<SampleVO> excelFile = new SheetExcelFile<>(SampleVO.class);
        String tableNm = "excel_data";
        int jdbcDataSize = jdbcRepository.getDataSize(tableNm);

        int splitSize = 10000;
        List<Object> dataList = new ArrayList<>();
        int iterSize = (jdbcDataSize / splitSize) + 1;
        for (int i = 0; i < iterSize; i++) {
            excelFile.addRows(jdbcRepository.getDataInfo(i, splitSize, tableNm, SampleVO.class));
        }

        System.out.println(dataList.size());

        List<SampleVO> retList = (List<SampleVO>) (List<?>) dataList;
        excelFile.addRows(retList);
        FileOutputStream stream = new FileOutputStream("/Users/ihong-gyu/Desktop/sample1.xlsx");
        excelFile.write(stream);
    }

    @Test
    @Description("엑셀 임시폴더 삭제 테스트")
    public void deleteUploadFolder() {
        deleteFolder(getUploadDirectoryPath());
    }

    public void deleteFolder(String path) {
        File deleteFolder = new File(path);

        if(deleteFolder.exists()){
            File[] deleteFolderList = deleteFolder.listFiles();

            for (int i = 0; i < deleteFolderList.length; i++) {
                if(deleteFolderList[i].isFile()) {
                    deleteFolderList[i].delete();
                }else {
                    deleteFolder(deleteFolderList[i].getPath());
                }
                deleteFolderList[i].delete();
            }
            deleteFolder.delete();
        }
    }

}
