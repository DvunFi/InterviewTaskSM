package com.example.interviewTaskSM;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.example.interviewTaskSM.domain.Message;
import com.example.interviewTaskSM.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class ParseControl {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/parser")
    public String add(Map<String, Object> model) throws IOException {

        List<Message> Messages = new ArrayList<Message>();
        String[] record = null;
        CSVReader reader = new CSVReader(new FileReader("src/main/CSVFiles/test_case.csv"), ';');
        reader.readNext();
        while ((record = reader.readNext()) != null) {
            Message mTMP = new Message();
            mTMP.setSsoid(record[0]);
            mTMP.setTs(record[1]);
            mTMP.setGrp(record[2]);
            mTMP.setType(record[3]);
            mTMP.setSubtype(record[4]);
            mTMP.setUrl(record[5]);
            mTMP.setOrgid(record[6]);
            mTMP.setFormid(record[7]);
            mTMP.setCode(record[8]);
            mTMP.setItpa(record[9]);
            mTMP.setSudirresponse(record[10]);
            Date d = null;
            try {
                d = new SimpleDateFormat("yyyy-MM-dd-HH").parse(record[11]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mTMP.setYmdh(d);
            Messages.add(mTMP);
        }
        reader.close();

        Iterable<Message> ms =  Messages;
        messageRepo.saveAll(ms);
        return "parser";
    }
}