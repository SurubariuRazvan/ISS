package com.exam.client.controller;

import com.exam.client.gui.GuiUtility;
import com.exam.domain.Employee;
import com.exam.domain.Employee_Paper;
import com.exam.domain.Paper;
import com.exam.service.AppServiceException;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class EmployeeController extends UserController<Employee> {
    public TableView<Employee_Paper> paperTable;
    public TableColumn<Employee_Paper, String> paperDescription;
    public TableColumn<Employee_Paper, Integer> paperID;
    public TableColumn<Employee_Paper, Double> paperGrade;
    public TableColumn<Employee_Paper, Void> gradeButton;
    public TableColumn<Employee_Paper, String> paperStatus;
    public Spinner<Double> gradeSpinner;
    protected ObservableList<Employee_Paper> entities;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paperID.setCellValueFactory(line -> new ReadOnlyObjectWrapper<>(line.getValue().getPaper().getId()));
        paperDescription.setCellValueFactory(line -> new ReadOnlyObjectWrapper<>(line.getValue().getPaper().getDescription()));
        paperGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        GuiUtility.addButtonToTable(gradeButton, "sendTask", () -> new MaterialDesignIconView(MaterialDesignIcon.SEND, "30"), (index, paper) -> {
            try {
                paper.setGrade(gradeSpinner.getValue());
                appService.updateGrade(paper);
            } catch (Exception e) {
                GuiUtility.showError(rootPane, menuPane, "Eroare", e.getMessage());
            }
        });
        paperStatus.setCellValueFactory(line -> {
            var employees = line.getValue().getPaper().getEmployees();
            Double grade1 = employees.get(0).getGrade();
            Double grade2 = employees.get(1).getGrade();
            double diff = abs(grade1 - grade2);
            if (!grade1.equals(0.0) && !grade2.equals(0.0)) {
                if (diff <= 1)
                    return new ReadOnlyObjectWrapper<>("OK " + diff);
                else
                    return new ReadOnlyObjectWrapper<>("REEVALUATE " + diff);
            } else
                return new ReadOnlyObjectWrapper<>("WAITING");
        });

        customiseFactory(paperStatus);
        GuiUtility.initSpinner(gradeSpinner, 0.0, 10.0);
    }

    private void customiseFactory(TableColumn<Employee_Paper, String> calltypel) {
        calltypel.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getItem());
                setGraphic(null);
                TableRow<Employee_Paper> currentRow = getTableRow();
                if (!isEmpty()) {
                    if (item.contains("OK"))
                        currentRow.setStyle("-fx-background-color:green");
                    else if (item.contains("REEVALUATE"))
                        currentRow.setStyle("-fx-background-color:red");
                }
            }
        });
    }

    protected void postInitialization() {
        try {
            var a = appService.findAllTasksForEmployee((Employee) user).stream().sorted(Comparator.comparing(item -> item.getPaper().getId())).collect(Collectors.toList());
            entities = FXCollections.observableList(a);
        } catch (AppServiceException e) {
            e.printStackTrace();
        }
        paperTable.setItems(entities);
    }


    @Override
    public void updateWindows(List<Employee_Paper> papers) {
        paperTable.setItems(FXCollections.observableList(papers.stream().sorted(Comparator.comparing(item -> item.getPaper().getId())).collect(Collectors.toList())));
    }
}
