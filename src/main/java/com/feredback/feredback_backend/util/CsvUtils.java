package com.feredback.feredback_backend.util;

import com.feredback.feredback_backend.entity.Candidate;
import com.feredback.feredback_backend.entity.vo.GradeVo;
import com.feredback.feredback_backend.service.ex.EmptyFileException;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-06 03:19
 **/
public class CsvUtils {
    private final static String PATH = System.getProperty("user.dir") + "/src/main/resources/csv";
    private static final String acceptedType = "text/csv";

    public static boolean isCsvFile(MultipartFile file) {
        if (Objects.equals(file.getContentType(), acceptedType)){
            return true;
        }
        return false;
    }

    public static CsvContainer readFromMultipartFile (MultipartFile multipartFile) {
        String path = PATH + "/" + multipartFile.getOriginalFilename();
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);
        File file = new File(path);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            CsvContainer csvContainer = csvReader.read(file, StandardCharsets.UTF_8);
            //check if the csv file is empty
            if (csvContainer == null) {
                throw new EmptyFileException("The CSV file you submitted is empty, please try again");
            }
            else {
                return csvContainer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @program: FE-Redback
     * @description:
     * @author: Hanlin Guo, StudentID:872416
     * @create: 2022-04-16 17:08
     **/
    public static void generateCandidatesCsv(List<Candidate> candidates) throws IOException {
        File file = new File(PATH + "/" + "Candidate_List.csv");
        //创建目录
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        //删除并创建新文件
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();

        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        fileWriter.write("Number,First Name,Middle Name,LastName,Email");
        for (Candidate can: candidates){
            String line = String.format("%d,%s,%s,%s,%s",
                    can.getId(),
                    can.getFirstName(),
                    can.getMiddleName()==null? "":can.getMiddleName(),
                    can.getLastName(),
                    can.getEmail());
            fileWriter.newLine();
            fileWriter.write(line);
        }
        fileWriter.close();
    }

    /**
     * @program: FE-Redback
     * @description:
     * @author: Hanlin Guo, StudentID:872416
     * @create: 2022-05-08 20:01
     **/
    public static void generateGradesCsv(List<GradeVo> grades) throws IOException {
        File file = new File(PATH + "/" + "Grade_List.csv");
        //创建目录
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        //删除并创建新文件
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();

        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        fileWriter.write("Student ID,Student Name,Grade,Mark");
        for (GradeVo grade:grades){
            String line;
            if (grade.getMark()==-1.0){
                line = String.format("%d,%s,No Mark,No Grade",
                        grade.getStudentId(),
                        grade.getStudentName());
            } else {
                line = String.format("%d,%s,%s,%f",
                        grade.getStudentId(),
                        grade.getStudentName(),
                        grade.getGrade(),
                        grade.getMark());
            }
            fileWriter.newLine();
            fileWriter.write(line);
        }
        fileWriter.close();
    }
}
