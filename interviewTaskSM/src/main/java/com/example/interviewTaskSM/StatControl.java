package com.example.interviewTaskSM;

import com.example.interviewTaskSM.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.sql.*;

import java.util.*;


@Controller
public class StatControl {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/topfiveforms")
    public String greeting(Map<String, Object> model) throws ClassNotFoundException {
        List<String> nameOfFormid = new ArrayList<String>();
        List<Integer> count = new ArrayList<Integer>();
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Messages","postgres", "");
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT formid,count(formid) FROM message group by (formid) order by formid limit 5");
            ResultSet result2 = preparedStatement.executeQuery();

            while (result2.next()) {
                nameOfFormid.add(result2.getString("formid"));
                count.add(result2.getInt("count"));
            }

            model.put("noDublicateResult",  nameOfFormid);
            model.put("count",  count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "topfiveform";
    }

    @GetMapping("/usersactiviti")
    public String usersActivity(Map<String, Object> model) throws ClassNotFoundException {
        List<String> nameOfFormid = new ArrayList<String>();
        List<String> count = new ArrayList<String>();
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Messages","postgres", "");
            PreparedStatement preparedStatement = null;
            //это для моих данных. там не получается чтолибо вывести за последний час так, как данные за 2017 год
            // сути этот вопрос попростувыводит что делал пользователь в маштабе всей БД. Я это сделал чтобы было,что выводить
            preparedStatement = connection.prepareStatement("SELECT ssoid, array_agg(DISTINCT formid ORDER BY formid) FROM message group by (ssoid) limit 100 ");
            // строка ниже за последний час
            //preparedStatement = connection.prepareStatement("SELECT ssoid, array_agg(DISTINCT formid ORDER BY formid) FROM message WHERE ymdh > now()- interval '1 hour' group by (ssoid)");

            ResultSet result2 = preparedStatement.executeQuery();

            while (result2.next()) {
                nameOfFormid.add(result2.getString("ssoid"));
                count.add(result2.getArray("array_agg").toString());
            }

            model.put("noDublicateResult",  nameOfFormid);
            model.put("count",  count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "usersActivity";
    }


    @GetMapping("/checkendactivity")
    public String checkForActivity(Map<String, Object> model) throws ClassNotFoundException {
        List<String> nameOfFormid = new ArrayList<String>();
        List<String> count = new ArrayList<String>();
       Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Messages","postgres", "");
            PreparedStatement preparedStatement = null;
            preparedStatement = connection.prepareStatement("SELECT ssoid, array_agg(subtype order by ts) FROM message Where (grp) like 'dszn_%'||formid group by ssoid having not(ARRAY['send']::varchar[] <@ array_agg(subtype order by ts))");

            ResultSet result2 = preparedStatement.executeQuery();

            while (result2.next()) {
                nameOfFormid.add(result2.getString("ssoid"));
               // count.add(result2.getArray("array_agg").);
                String[] array =(String []) result2.getArray("array_agg").getArray();
                count.add(array[array.length-1]);

            }

            model.put("noDublicateResult",  nameOfFormid);
            model.put("count",  count);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "checkActivityForEnd";
    }

}
