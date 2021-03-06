package dev.excel.controller;

import dev.excel.dto.SampleVO;
import dev.excel.service.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import static dev.excel.utils.DataUtils.executeTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DownloadController {

    private final DownloadService downloadService;

    @GetMapping(value = "/download", produces = "application/text; charset=UTF-8")
    public void excelDownload(HttpServletResponse response, @RequestParam("title") String title) {
        long start = System.currentTimeMillis();
        log.info("============= [DOWNLOAD START] =============");

        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        /**
         * SET VO.class
         */
        Class<SampleVO> clazz = SampleVO.class;

        if (title.equals("jdbc")) {
            downloadService.excelDownloadJdbc(response, clazz);
        } else if (title.equals("myBatis")) {
            downloadService.excelDownloadMybatis(response, clazz);
        } else if (title.equals("jpa")) {
            downloadService.excelDownloadJpa(response, clazz);
        }

        long end = System.currentTimeMillis();
        log.info("EXECUTE TIME => {}(s)", executeTime(start, end));
    }
}
