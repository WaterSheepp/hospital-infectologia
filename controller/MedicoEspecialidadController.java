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
import org.alvaroramirez.bean.MedicoEspecialidad;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;



public class MedicoEspecialidadController implements Initializable{
     private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoOperaciones = operaciones.Ninguno;
 private ObservableList <MedicoEspecialidad> listarMedicoEspecialidad;
 
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
 public void ventanaPacientes(){
 escenarioPrincipal.ventanaPacientes();
 }
    public void ventanaHorarios (){
   escenarioPrincipal.ventanaHorarios();
   }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedicoEspecialidad.setItems(getMedicoEspecialidad());
        
    }
    @FXML private ComboBox cmbCodigoMedicoEspecialidad;
     @FXML private TextField txtCodigoMedico;
        @FXML private TextField txtCodigoEspecialidad;
        @FXML private TextField txtCodigoHorario;
        
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        
        @FXML private TableView tblMedicoEspecialidad;
        @FXML private TableColumn colCodigoMedicoEspecialidad;
        @FXML private TableColumn colCodigoMedico;
        @FXML private TableColumn colCodigoEspecialidad;
        @FXML private TableColumn colCodigoHorario;
        

 public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidad());
        colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoMedicoEspecialidad"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoMedico"));
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoEspecialidad"));
        colCodigoHorario.setCellValueFactory(new PropertyValueFactory <MedicoEspecialidad,Integer>("codigoHorario"));
     }
                public ObservableList <MedicoEspecialidad> getMedicoEspecialidad(){
    ArrayList <MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
    
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMedicosEspecialidades}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                resultado.getInt("codigoMedico"),
                resultado.getInt("codigoEspecialidad"),
                resultado.getInt("codigoHorario")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarMedicoEspecialidad = FXCollections.observableList(lista);
    }
     public void seleccionar (){
    cmbCodigoMedicoEspecialidad.getSelectionModel().select(buscar(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
    txtCodigoMedico.setText(String.valueOf(((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
    txtCodigoEspecialidad.setText(String.valueOf(((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
    txtCodigoHorario.setText(String.valueOf(((MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
    
    }
 public MedicoEspecialidad buscar(int codigoMedicoEspecialidad){
     MedicoEspecialidad resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_MedicoEspecialidades(?)}");
         procedimiento.setInt(1,codigoMedicoEspecialidad);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new MedicoEspecialidad (registro.getInt("codigoMedicoEspecialidad"),
                registro.getInt("codigoMedico"),
                registro.getInt("codigoEspecialidad"),
                registro.getInt("codigoHorario"));
         }
     }catch(Exception e){
         e.printStackTrace();
         e.getMessage();
     }
    return resultado;
 }
  public void eliminar (){
        switch(tipoOperaciones){
        
            case Guardar:
                desactivar();
                limpiar();
                btnAgregar.setText("✚");
                btnEliminar.setText("✖");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                tipoOperaciones = operaciones.Nuevo;
                break;
            default:
                if (tblMedicoEspecialidad.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar MedicoEspecialidad ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicoEspecialidades (?)}");
                            procedimiento.setInt(1,((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                            procedimiento.execute();
                            listarMedicoEspecialidad.remove(tblMedicoEspecialidad.getSelectionModel().getSelectedIndex());
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
  
   public void editar (){
    switch (tipoOperaciones){
    
        case Ninguno:
            if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem()!= null){
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            tipoOperaciones = operaciones.Actualizar;
            btnAgregar.setDisable(true);
            btnEliminar.setDisable(true);
            activar();
                
            break;
            }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
            break;
            case Actualizar:
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar MedicoEspecialidad ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                
                actualizar();
                
                limpiar();
                btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoOperaciones = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                
                cargarDatos();
                break;
    }else{  btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoOperaciones = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                     desactivar();
            }
        }
    }
    public  void nuevo(){
     
     switch (tipoOperaciones){
         case Ninguno:
             activar();
             btnAgregar.setText("Guardar");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReporte.setDisable(true);
            
             tipoOperaciones = operaciones.Guardar  ;
         break;
         
         case Guardar:
             guardar();
              desactivar();
                limpiar();
                   btnAgregar.setText("✚");
                   btnEliminar.setText("✖");
                  btnEditar.setDisable(false);
             btnReporte.setDisable(false); 
                  
                tipoOperaciones = operaciones.Ninguno;
             cargarDatos();
             break;
                       }
 }
     public void actualizar (){
 try{
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarMedicoEspecialidades(?,?,?,?)}");
     MedicoEspecialidad registro =(MedicoEspecialidad) tblMedicoEspecialidad.getSelectionModel().getSelectedItem();
     registro.setCodigoMedicoEspecialidad(Integer.parseInt(cmbCodigoMedicoEspecialidad.getSelectionModel().getSelectedItem().toString()));
     registro.setCodigoMedico(Integer.parseInt(txtCodigoMedico.getText()));
     registro.setCodigoEspecialidad(Integer.parseInt(txtCodigoEspecialidad.getText()));
     registro.setCodigoHorario(Integer.parseInt(txtCodigoHorario.getText()));
     
     procedimiento.setInt(1,registro.getCodigoMedicoEspecialidad());
     procedimiento.setInt(2, registro.getCodigoMedico());
     procedimiento.setInt(3, registro.getCodigoEspecialidad());
     procedimiento.setInt(4, registro.getCodigoHorario());
     
     procedimiento.execute();
     
 
 
 }catch(SQLException e){
     e.printStackTrace();
     }
 }
      public void activar(){
          
          cmbCodigoMedicoEspecialidad.setDisable(false);
          txtCodigoMedico.setDisable(false);
          txtCodigoEspecialidad.setDisable(false);
          txtCodigoHorario.setDisable(false);
            
            cmbCodigoMedicoEspecialidad.setEditable(true);
            txtCodigoMedico.setEditable(true);
            txtCodigoEspecialidad.setEditable(true);
            txtCodigoHorario.setEditable(true);
        }      
        public void desactivar() {
        cmbCodigoMedicoEspecialidad.setDisable(true);
          txtCodigoMedico.setDisable(true);
          txtCodigoEspecialidad.setDisable(true);
          txtCodigoHorario.setDisable(true);
          
             cmbCodigoMedicoEspecialidad.setEditable(false);
            txtCodigoMedico.setEditable(false);
            txtCodigoEspecialidad.setEditable(false);
            txtCodigoHorario.setEditable(false);
        }      
        
        
    
 public void limpiar(){
            txtCodigoMedico.setText("");
             txtCodigoEspecialidad.setText("");
             txtCodigoHorario.setText("");
 }
         public void guardar() {   
        MedicoEspecialidad registro = new MedicoEspecialidad();
        registro.setCodigoMedico(Integer.parseInt(txtCodigoMedico.getText()));
        registro.setCodigoEspecialidad(Integer.parseInt(txtCodigoEspecialidad.getText()));
        registro.setCodigoHorario(Integer.parseInt(txtCodigoHorario.getText()));
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidades (?,?,?)}");
     procedimiento.setInt(1, registro.getCodigoMedico());
     procedimiento.setInt(2, registro.getCodigoEspecialidad());
     procedimiento.setInt(3, registro.getCodigoHorario());
     procedimiento.execute();
     listarMedicoEspecialidad.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}    
       public void imprimirReporte(){
 if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem()!= null){
 int codigoMedicoEspecialidad = ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad();
    Map parametros = new HashMap();
    parametros.put("p_codigoMedicoEspecialidad", codigoMedicoEspecialidad);
    GenerarReporte.mostrarReporte("MedicoEspecialidadreport.jasper", "Reporte MedicoEspecialidad ", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }     
                
}

