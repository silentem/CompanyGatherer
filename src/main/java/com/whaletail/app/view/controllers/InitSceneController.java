package com.whaletail.app.view.controllers;

import com.whaletail.app.County;
import com.whaletail.app.data.gather.DataGather;
import com.whaletail.app.data.gather.exceptions.QuotaLimitException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.util.*;

import static com.whaletail.app.view.util.ViewUtil.alert;

/**
 * @author Whaletail
 */
public class InitSceneController implements Initializable {
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static final String LAST_COUNTY_ID = "last_county_id";
    private static final String LAST_COMPANY_NUMBER = "last_company_number";
    private static final String JOB_DONE = "job_done";
    public static final String COUNTY_CSV = "county_lat_lng.csv";

    @FXML
    public TextField locationField;
    @FXML
    public TextField queryField;
    @FXML
    public Button initButton;
    @FXML
    public TextArea outputArea;
    @FXML
    public Button saveButton;
    @FXML
    public BorderPane pane;
    private Map<String, String> companies;

    private long last_county;
    private int last_company;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        try {
//            if (!locationsGathered()) {
//                outputArea.setText("Gathering location");
//                DataGather dataGather = new DataGather();
//                List<County> counties = dataGather.gatherLocations(getCounties());
//                BufferedWriter writer = null;
//                try {
//                    writer = new BufferedWriter(new FileWriter("county_lat_lng.csv", false));
//                    for (County county : counties) {
//                        if (county != null){
//
//                            String line =
//                                    Long.toString(county.getId()) + ", " +
//                                            county.getName() + ", " +
//                                            county.getLat() + ", " +
//                                            county.getLng();
//                            writer.write(line);
//                            writer.newLine();
//                        }
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (writer != null) {
//                        try {
//                            writer.close();
//                        } catch (IOException ex) {
//                            System.out.println(ex.getMessage());
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(CONFIG_PROPERTIES);
            prop.load(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        last_county = Long.parseLong(prop.getProperty(LAST_COUNTY_ID));
        last_company = Integer.parseInt(prop.getProperty(LAST_COMPANY_NUMBER));

        queryField.setText("web companies");
        locationField.setEditable(false);
        locationField.setText(getCounty(last_county).getName());
        outputArea.setEditable(false);


        Gatherer gatherer = new Gatherer();

        initButton.setOnAction(event -> {
            String address = locationField.getText().replace(" ", "+");
            String query = queryField.getText().replace(" ", "+");
            gatherer.setAddress(address);
            gatherer.setQuery(query);
            gatherer.start();
        });

        saveButton.setOnAction(event -> gatherer.setGather(false));

    }

    private boolean locationsGathered() throws IOException {
        BufferedReader reader =
                new BufferedReader(new FileReader(COUNTY_CSV));
        String header = reader.readLine();
        String[] split = header.split(",");
        int gathered = 0;
        for (String key : split) {
            if (key.equals("LNG") || key.equals("LAT")) {
                gathered++;
            }
        }
        return gathered == 2;
    }

    private String quotes(String str) {
        return '\"' + str + '\"';
    }

    private List<County> getCounties() throws IOException {
        List<County> counties = new ArrayList<>();
        try {

            String countyLine = "";
            BufferedReader reader =
                    new BufferedReader(new FileReader(COUNTY_CSV));
            reader.readLine();
            while ((countyLine = reader.readLine()) != null) {
                String[] split = countyLine.split(",");
                counties.add(new County(Long.parseLong(split[0]),
                        Integer.parseInt(split[1]),
                        split[2].replace("\"", ""),
                        0,
                        0));
            }
        } catch (NullPointerException e) {
            alert(new Alert(Alert.AlertType.INFORMATION),
                    "Can not find file",
                    null,
                    "Can not find county.csv file. Please check if it exists!",
                    pane.getScene(),
                    new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE)).showAndWait();
        }
        return counties;
    }

    private County getCounty(long lineNumber) {
        String line = "";
        try {
            List<String> counties = new ArrayList<>();
            try {
                String countyLine = "";
                BufferedReader reader =
                        new BufferedReader(new FileReader(COUNTY_CSV));
                while ((countyLine = reader.readLine()) != null) {
                    counties.add(countyLine);
                }
            } catch (NullPointerException e) {
                alert(new Alert(Alert.AlertType.INFORMATION),
                        "Can not find file",
                        null,
                        "Can not find county.csv file. Please check if it exists!",
                        pane.getScene(),
                        new ButtonType("ОК", ButtonBar.ButtonData.CANCEL_CLOSE)).showAndWait();
            }
            if (counties.size() <= lineNumber) {
                return new County(-1L, -1, JOB_DONE, -1, -1);
            }
            line = counties.get(Math.toIntExact(lineNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] split = line.split(",");
        return new County(Long.parseLong(split[0]),
                Integer.parseInt(split[1]),
                split[2].replace("\"", ""),
                0,
                0);
    }

    private void saveData() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("memory.csv", true));

            List<Map.Entry<String, String>> entries = new ArrayList<>(companies.entrySet());
            for (int i = last_company; i < entries.size(); i++) {
                writer.write(quotes(locationField.getText()) + ", " +
                        quotes(queryField.getText()) + ", " +
                        quotes(entries.get(i).getKey()) + ", " +
                        quotes(entries.get(i).getValue()));
                writer.newLine();
            }
            saveProperties();
            companies.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    private void saveProperties() {
        OutputStream os = null;
        try {
            Properties prop = new Properties();
            os = new FileOutputStream(CONFIG_PROPERTIES);
            prop.setProperty(LAST_COUNTY_ID, String.valueOf(last_county));
            prop.setProperty(LAST_COMPANY_NUMBER, String.valueOf(last_company));
            prop.store(os, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private class Gatherer extends Thread {

        private String address;
        private String query;

        private boolean gather = true;

        Gatherer() {
        }

        Gatherer(String address, String query) {
            this.address = address;
            this.query = query;
            setDaemon(true);
        }

        public boolean isGather() {
            return gather;
        }

        public void setGather(boolean gather) {
            this.gather = gather;
        }

        private void setAddress(String address) {
            this.address = address;
        }

        private void setQuery(String query) {
            this.query = query;
        }

        @Override
        public void run() {
            DataGather dataGather = new DataGather();
            try {
                while (isGather()) {
                    outputArea.setText(outputArea.getText() + "\nPlease wait... Gathering data...");
                    Pair<Map<String, String>, QuotaLimitException> gather = dataGather.gather(address, query);
                    companies = gather.getKey();
                    if (gather.getValue() != null) {
                        outputArea.setText("Request limit is exhausted. Please continue next day from 10:00 ");
                        last_company = companies.size() - 1;
                        saveData();
                        break;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        companies.forEach((s, s2) ->
                                sb.append(s).append(": ").append(s2).append("\r\n"));
                        last_county++;
                        last_company = 0;
                        outputArea.setText(sb.toString());
                        saveData();
                        String country = getCounty(last_county).getName();
                        locationField.setText(country);
                        this.setAddress(country.replace(" ", "+"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
