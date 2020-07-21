

package org.alvaroramirez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alvaroramirez.bean.Areas;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;


public class AreasController implements Initializable{
    
 private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoDeOperacion = operaciones.Ninguno;
 private ObservableList <Areas> listarAreas;
 
 private  Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
  public void menuPrincipal (){
    escenarioPrincipal.menuPrincipal();
}
  public void ventanaResponsableTurno(){
   
   escenarioPrincipal.ventanaResponsableTurno();
   }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
    }
    @FXML private ComboBox cmbCodigoArea;
        @FXML private TextField txtAreas;      
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblAreas;
        @FXML private TableColumn colCodigoArea;
        @FXML private TableColumn colNombreArea;

public void cargarDatos(){
        
        tblAreas.setItems(getAreas());
            colCodigoArea.setCellValueFactory(new PropertyValueFactory <Areas,Integer>("codigoArea"));
                colNombreArea.setCellValueFactory(new PropertyValueFactory <Areas,String>("nombreArea"));
     }
 public ObservableList <Areas> getAreas(){
    ArrayList <Areas> lista = new ArrayList<Areas>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarAreas}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Areas(resultado.getInt("codigoArea"),
                resultado.getString("nombreArea")));
               
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarAreas = FXCollections.observableList(lista);
    }
    public void seleccionar (){
    cmbCodigoArea.getSelectionModel().select(buscar(((Areas)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea()));
    txtAreas.setText((((Areas) tblAreas.getSelectionModel().getSelectedItem()).getNombreArea()));
    
    }
     public Areas buscar(int codigoArea){
     Areas resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Areas(?)}");
         procedimiento.setInt(1,codigoArea);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Areas (registro.getInt("codigoArea"),
                                            registro.getString("nombreArea"));
         }
     
     }catch(Exception e){
         e.printStackTrace();
     }
 
    return resultado;
 }
         public void eliminar (){
        switch(tipoDeOperacion){
        
            case Guardar:
                desactivar();
                limpiar();
              btnAgregar.setText("✚");
                   btnEliminar.setText("✖");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tipoDeOperacion = operaciones.Nuevo;
                break;
            default:
                if (tblAreas.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Area ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas (?)}");
                            procedimiento.setInt(1,((Areas)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                            procedimiento.execute();
                            listarAreas.remove(tblAreas.getSelectionModel().getSelectedIndex());
                            cargarDatos();
                            limpiar();
                            
                       }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
        }    
 }
      public  void nuevo(){
     
     switch (tipoDeOperacion){
         case Ninguno:
             
             activar();
             btnAgregar.setText("Guardar");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReporte.setDisable(true);
            
              tipoDeOperacion = operaciones.Guardar  ;
         break;
         
         case Guardar:
             guardar();
              desactivar();
                limpiar();
                    btnAgregar.setText("✚");
                   btnEliminar.setText("✖");
                  btnEditar.setDisable(false);
             btnReporte.setDisable(false); 
                  
                tipoDeOperacion = operaciones.Ninguno;
             cargarDatos();
             break;
                       }
 }
        public void editar (){
    switch (tipoDeOperacion){
    
        case Ninguno:
            if(tblAreas.getSelectionModel().getSelectedItem()!= null){
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            tipoDeOperacion = operaciones.Actualizar;
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            activar();
                
            break;
            }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
            break;
            case Actualizar:
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Paciente ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                actualizar();
                
                limpiar();
           btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoDeOperacion = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                
                cargarDatos();
                break;
    }else{     btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoDeOperacion = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                     desactivar();
            }
        }
    }
     
      public void actualizar (){
 try{
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarAreas(?,?)}");
     Areas registro =(Areas) tblAreas.getSelectionModel().getSelectedItem();
     registro.setCodigoArea(Integer.parseInt(cmbCodigoArea.getSelectionModel().getSelectedItem().toString()));
 registro.setNombreArea((txtAreas.getText()));
     
     procedimiento.setInt(1,registro.getCodigoArea());
     procedimiento.setString(2, registro.getNombreArea());
     procedimiento.execute();
     
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
        public void activar(){
          cmbCodigoArea.setDisable(false);
          txtAreas.setDisable(false);
    
          cmbCodigoArea.setEditable(true);
          txtAreas.setEditable(true);
        }    
         public void limpiar(){
            txtAreas.setText("");
 }
          public void guardar() {   
        Areas registro = new Areas();
        registro.setNombreArea(txtAreas.getText());
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas (?)}");
     procedimiento.setString(1, registro.getNombreArea());
     procedimiento.execute();
     listarAreas.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
    public void desactivar() {
       cmbCodigoArea.setDisable(true);
        txtAreas.setDisable(true);
          cmbCodigoArea.setEditable(false);
            txtAreas.setEditable(false);
    }
     public void imprimirReporte(){
 if(tblAreas.getSelectionModel().getSelectedItem()!= null){
 int codigoArea = ((Areas)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea();
    Map parametros = new HashMap();
    parametros.put("p_codigoArea", codigoArea);
    GenerarReporte.mostrarReporte("AreasReport.jasper", "Reporte Areas", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
}
