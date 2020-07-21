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
import org.alvaroramirez.bean.Cargos;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;


public class CargosController  implements Initializable{
    private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoDeOperacion = operaciones.Ninguno;
 private ObservableList <Cargos> listarCargos;
 
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
        cmbCodigoCargo.setItems(getCargos());
    }
    @FXML private ComboBox cmbCodigoCargo;
        @FXML private TextField txtCargo;      
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblCargos;
        @FXML private TableColumn colCodigoCargo;
        @FXML private TableColumn colNombreCargo;

public void cargarDatos(){
        
        tblCargos.setItems(getCargos());
            colCodigoCargo.setCellValueFactory(new PropertyValueFactory <Cargos,Integer>("codigoCargo"));
                colNombreCargo.setCellValueFactory(new PropertyValueFactory <Cargos,String>("nombreCargo"));
     }
 public ObservableList <Cargos> getCargos(){
    ArrayList <Cargos> lista = new ArrayList<Cargos>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarCargos}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Cargos(resultado.getInt("codigoCargo"),
                resultado.getString("nombreCargo")));
               
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarCargos = FXCollections.observableList(lista);
    }
    public void seleccionar (){
    cmbCodigoCargo.getSelectionModel().select(buscar(((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo()));
    txtCargo.setText((((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo()));
    
    }
     public Cargos buscar(int codigoCargo){
     Cargos resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Cargos(?)}");
         procedimiento.setInt(1,codigoCargo);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Cargos (registro.getInt("codigoCargo"),
                                            registro.getString("nombreCargo"));
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
                if (tblCargos.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Cargos ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargos (?)}");
                            procedimiento.setInt(1,((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                            procedimiento.execute();
                            listarCargos.remove(tblCargos.getSelectionModel().getSelectedIndex());
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
            if(tblCargos.getSelectionModel().getSelectedItem()!= null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Cargos ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarCargos(?,?)}");
     Cargos registro =(Cargos) tblCargos.getSelectionModel().getSelectedItem();
     registro.setCodigoCargo(Integer.parseInt(cmbCodigoCargo.getSelectionModel().getSelectedItem().toString()));
     registro.setNombreCargo((txtCargo.getText()));
     
     procedimiento.setInt(1,registro.getCodigoCargo());
     procedimiento.setString(2, registro.getNombreCargo());
     procedimiento.execute();
     
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
        public void activar(){
          cmbCodigoCargo.setDisable(false);
          txtCargo.setDisable(false);
    
          cmbCodigoCargo.setEditable(true);
          txtCargo.setEditable(true);
        }    
         public void limpiar(){
            txtCargo.setText("");
 }
          public void guardar() {   
        Cargos registro = new Cargos();
        registro.setNombreCargo(txtCargo.getText());
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos (?)}");
     procedimiento.setString(1, registro.getNombreCargo());
     procedimiento.execute();
     listarCargos.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
    public void desactivar() {
       cmbCodigoCargo.setDisable(true);
        txtCargo.setDisable(true);
          cmbCodigoCargo.setEditable(false);
            txtCargo.setEditable(false);
    }
     public void imprimirReporte(){
 if(tblCargos.getSelectionModel().getSelectedItem()!= null){
 int codigoCargo = ((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo();
    Map parametros = new HashMap();
    parametros.put("p_codigoCargo", codigoCargo);
    GenerarReporte.mostrarReporte("Cargosreport.jasper", "Reporte Cargos", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
}


