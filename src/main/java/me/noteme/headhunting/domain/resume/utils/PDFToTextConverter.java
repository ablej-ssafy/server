package me.noteme.headhunting.domain.resume.utils;

import me.noteme.headhunting.common.exception.CustomException;
import me.noteme.headhunting.common.exception.ErrorCode;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PDFToTextConverter {
    public String convertPdfToText(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getInputStream().readAllBytes())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setAddMoreFormatting(true); // 텍스트 형식 보존
            pdfStripper.setSortByPosition(true); // 페이지 순서 정렬
            return pdfStripper.getText(document);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_TO_CONVERTER_PDF);
        }
    }
}
