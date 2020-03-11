package com.shdq.OPCUA_YML_gen_platform.controller;

import com.shdq.OPCUA_YML_gen_platform.MainApp;
import com.shdq.OPCUA_YML_gen_platform.model.ListenerPair;
import com.shdq.OPCUA_YML_gen_platform.model.OpcUaProperties;
import com.shdq.OPCUA_YML_gen_platform.util.CommonUtil;
import com.shdq.OPCUA_YML_gen_platform.util.ThemeUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.controlsfx.dialog.ExceptionDialog;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ShowListenerPairController {

    @Setter
    private Stage dialogStage;

    @Setter
    private MainApp mainApp;

    @Setter
    private int plcIndex;

    private List<ListenerPair> pairs;

    ObservableList<ListenerPair> pairsDate = FXCollections.observableArrayList();

    @FXML
    private TextField listenerName;

    @FXML
    private TextArea listenerNodeStr;

    @FXML
    private TableView<ListenerPair> listenerTable;

    @FXML
    private TableColumn<ListenerPair,String> idColumn;

    @FXML
    private TableColumn<ListenerPair,String> listenerNameColumn;

    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(cellDate ->
            cellDate.getValue().idProperty()
        );
        listenerNameColumn.setCellValueFactory(cellDate ->
            cellDate.getValue().listenerNameProperty()
        );
        listenerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showListenerPairDetail(newValue));
    }

    public void setPairs(List<ListenerPair> pairs) {
        this.pairs = pairs;
        setData();
    }

    private void setData(){
        pairsDate.addAll(pairs);
        listenerTable.setItems(pairsDate);
    }

    private void showListenerPairDetail(ListenerPair pair) {
        if (pair != null){
            listenerName.setText(pair.getListenerName());
            listenerNodeStr.setText(pair.getNodeStr());
        }else {
            listenerName.setText("");
            listenerNodeStr.setText("");
        }
    }

    @FXML
    private void handleNewListenerPair(){
        ListenerPair tempPair = new ListenerPair();
        boolean okClicked = editListenerPairDialog(tempPair);
        if (okClicked){
            tempPair.setId(String.valueOf(listenerTable.getItems().size()+1));
            listenerTable.getItems().add(tempPair);
        }
    }

    private boolean editListenerPairDialog(ListenerPair tempPair) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/EditListenerPairDialog.fxml"));
            AnchorPane pane = loader.load();
            ThemeUtil.setMainTheme(pane);
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            if (StringUtils.isBlank(tempPair.getId())){
                dialogStage.setTitle("新增监听器对");
            }else {
                dialogStage.setTitle("编辑监听器对");
            }
            dialogStage.getIcons().add(new Image(CommonUtil.image_url));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            CommonUtil.setDialogWindowCoordinate(mainApp.getPrimaryStage(),dialogStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditListenerPairController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPair(tempPair);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void  handleEditListenerPair(){
        ListenerPair selectedListenerPair = listenerTable.getSelectionModel().getSelectedItem();
        if (selectedListenerPair != null){
            boolean okClicked = editListenerPairDialog(selectedListenerPair);
            if (okClicked){
                showListenerPairDetail(selectedListenerPair);
            }
        }else {
            ExceptionDialog dialog = new ExceptionDialog(new Throwable("Please select one in the table"));
            dialog.setTitle("No Selection");
            dialog.setHeaderText("No One Selected");
            dialog.show();
        }
    }

    @FXML
    private void handleDeleteListenerPair(){
        int selectIndex = listenerTable.getSelectionModel().getSelectedIndex();
        if (selectIndex >= 0){
            listenerTable.getItems().remove(selectIndex);
        }else {
            ExceptionDialog dialog = new ExceptionDialog(new Throwable("Please select one in the table"));
            dialog.setTitle("No Selection");
            dialog.setHeaderText("No One Selected");
            dialog.show();
        }
    }

    @FXML
    private void handleSaveListenerPairs(){
        updateListenerPairs(listenerTable.getItems());
        log.debug(OpcUaProperties.getProperties(mainApp).toString());
        dialogStage.close();
    }

    private void updateListenerPairs(ObservableList<ListenerPair> items) {
        OpcUaProperties properties = OpcUaProperties.getProperties(mainApp);
        Map<String,String> plcMap = properties.getPlcList().get(plcIndex);
        Set<Map.Entry<String,String>> set = plcMap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry<String,String>)iterator.next();
            if (!CommonUtil.notListenerNames.contains(entry.getKey())){
                iterator.remove();
            }
        }
        items.forEach(pair -> plcMap.put(pair.getListenerName(),pair.getNodeStr()));
    }
}
