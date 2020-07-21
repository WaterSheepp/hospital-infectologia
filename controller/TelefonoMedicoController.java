
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
import org.alvaroramirez.bean.TelefonoMedico;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class TelefonoMedicoController implements Initializable{
 private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoDeOperacion = operaciones.Ninguno;
 private ObservableList <TelefonoMedico> listarTelefonoMedicos;
 
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
     public void ventanaMedicos(){
 escenarioPrincipal.ventanaMedicos();
 }
       public void ventanaEspecialidad(){
 escenarioPrincipal.ventanaEspecialidad();
 }
 
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigo.setItems(getTelefonoMedico());
        
    }
    @FXML private ComboBox cmbCodigo;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private TextField txtCodigoMedico;
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblTelefonoMedico;
        @FXML private TableColumn colCodigoTelefono;
        @FXML private TableColumn colCodigoMedico;
        @FXML private TableColumn colTelefonoPersonal;
        @FXML private TableColumn colTelefonoTrabajo;

public void cargarDatos(){
        
        tblTelefonoMedico.setItems(getTelefonoMedico());
            colCodigoTelefono.setCellValueFactory(new PropertyValueFactory <TelefonoMedico,Integer>("codigoTelefonoMedico"));
                colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory <TelefonoMedico,String>("telefonoPersonal"));
                 colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory <TelefonoMedico,String>("telefonoTrabajo"));
                 colCodigoMedico.setCellValueFactory(new PropertyValueFactory <TelefonoMedico,Integer>("codigoMedico"));
     }
    public ObservableList <TelefonoMedico> getTelefonoMedico(){
    ArrayList <TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrartelefonosMedico}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                resultado.getString("telefonoPersonal"),
                resultado.getString("telefonoTrabajo"),
                resultado.getInt("codigoMedico")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarTelefonoMedicos = FXCollections.observableList(lista);
    }
    public void seleccionar (){
    cmbCodigo.getSelectionModel().select(buscar(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico()));
     txtTelefonoPersonal.setText((((TelefonoMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoPersonal()));
    txtTelefonoTrabajo.setText(((TelefonoMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
    txtCodigoMedico.setText(String.valueOf(((TelefonoMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico()));
    
    }
     public TelefonoMedico buscar(int codigoMedico){
     TelefonoMedico resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_telefonosMedico (?)}");
         procedimiento.setInt(1,codigoMedico);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new TelefonoMedico (registro.getInt("codigoTelefonoMedico"),
                                            registro.getString("telefonoPersonal"),
                                            registro.getString("telefonoTrabajo"),
                                            registro.getInt("codigoMedico"));
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
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tipoDeOperacion = operaciones.Nuevo;
                break;
            default:
                if (tblTelefonoMedico.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Telefono ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonosMedico (?)}");
                            procedimiento.setInt(1,((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                            procedimiento.execute();
                            listarTelefonoMedicos.remove(tblTelefonoMedico.getSelectionModel().getSelectedIndex());
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
                   btnAgregar.setText("Nuevo");
                   btnEliminar.setText("Eliminar");
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
            if(tblTelefonoMedico.getSelectionModel().getSelectedItem()!= null){
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
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                
                cargarDatos();
                break;
    }else{  btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                     desactivar();
            }
        }
    }
          
           public void actualizar (){
 try{
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificartelefonosMedico(?,?,?,?)}");
     TelefonoMedico registro =(TelefonoMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem();
     registro.setCodigoMedico(Integer.parseInt(cmbCodigo.getSelectionModel().getSelectedItem().toString()));
      registro.setCodigoMedico(Integer.parseInt(txtCodigoMedico.getText()));
      registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
      registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
    
      procedimiento.setInt(1, registro.getCodigoTelefonoMedico()); 
    procedimiento.setString(2, registro.getTelefonoPersonal());
     procedimiento.setString(3, registro.getTelefonoTrabajo());
     procedimiento.setInt(4, registro.getCodigoMedico());
     procedimiento.execute();
     
 
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
        public void activar(){
          cmbCodigo.setDisable(false);
          txtTelefonoPersonal.setDisable(false);
          txtTelefonoTrabajo.setDisable(false);
          txtCodigoMedico.setDisable(false);
          
            
          cmbCodigo.setEditable(true);
            txtTelefonoPersonal.setEditable(true);
            txtTelefonoTrabajo.setEditable(true);
            txtCodigoMedico.setEditable(true);
        }    
        public void desactivar() {
           cmbCodigo.setDisable(true);
            txtTelefonoPersonal.setDisable(true);
          txtTelefonoTrabajo.setDisable(true);
          txtCodigoMedico.setDisable(true);
          
          cmbCodigo.setEditable(false);
            txtTelefonoPersonal.setEditable(false);
            txtTelefonoTrabajo.setEditable(false);
            txtCodigoMedico.setEditable(false);
    }
         public void limpiar(){
            txtTelefonoPersonal.setText("");
                txtTelefonoTrabajo.setText("");
                txtCodigoMedico.setText("");
 }
          public void guardar() {   
        TelefonoMedico registro = new TelefonoMedico();
        registro.setCodigoMedico(Integer.parseInt(txtCodigoMedico.getText()));
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonosMedico (?,?,?)}");
     procedimiento.setString(1, registro.getTelefonoPersonal());
     procedimiento.setString(2, registro.getTelefonoTrabajo());
     procedimiento.setInt(3, registro.getCodigoMedico());
     procedimiento.execute();
     listarTelefonoMedicos.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
           public void imprimirReporte(){
 if(tblTelefonoMedico.getSelectionModel().getSelectedItem()!= null){
 int codigoMedico = ((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico();
    Map parametros = new HashMap();
    parametros.put("p_codigoHorario", codigoMedico);
    GenerarReporte.mostrarReporte("telefonoMedicoReport.jasper", "Reporte Telefono Medico", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
   
      
         
}