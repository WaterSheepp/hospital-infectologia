package org.alvaroramirez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alvaroramirez.bean.Medico;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.sistema.Principal;
import org.alvaroramirez.report.GenerarReporte;


public class  MedicoController implements Initializable{

 private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoOperaciones = operaciones.Ninguno;
 private ObservableList <Medico> listarMedicos;
 
 private  Principal escenarioPrincipal;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigo.setItems(getMedicos());
        
        dtpHoraEntrada = new DatePicker (Locale.ENGLISH);
        dtpHoraEntrada.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpHoraEntrada.getCalendarView().todayButtonTextProperty().set("Today");
        grpHoraEntrada.add(dtpHoraEntrada,0,0);
        
          
        dtpHoraSalida = new DatePicker (Locale.ENGLISH);
        dtpHoraSalida.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpHoraSalida.getCalendarView().todayButtonTextProperty().set("Today");
        grpHoraSalida.add(dtpHoraSalida,0,0);
      
    }
      private DatePicker dtpHoraSalida;
         private DatePicker dtpHoraEntrada;
        @FXML private ComboBox cmbCodigo;
        @FXML private TextField txtLicencia;
        @FXML private TextField txtNombre;
        @FXML private TextField txtApellidos;
        @FXML private TextField txtSexo;
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private GridPane grpHoraSalida;
        @FXML private GridPane grpHoraEntrada;
        @FXML private TableView tblMedicos;
        @FXML private TableColumn colCodigo;
        @FXML private TableColumn colLicencia;
        @FXML private TableColumn colNombres;
        @FXML private TableColumn colApellidos;
        @FXML private TableColumn colHoraEntrada;
        @FXML private TableColumn colHoraSalida; 
        @FXML private TableColumn colSexo;
        
        
    
       
    public void cargarDatos(){
        
        tblMedicos.setItems(getMedicos());
        colCodigo.setCellValueFactory(new PropertyValueFactory <Medico,Integer>("codigoMedico"));
        colLicencia.setCellValueFactory(new PropertyValueFactory <Medico,Integer>("licenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory <Medico,String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory <Medico,String>("apellidos"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory <Medico,Date>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory <Medico,Date>("horaSalida"));
        colSexo.setCellValueFactory(new PropertyValueFactory <Medico,String>("sexo"));
     }
    
    public ObservableList <Medico> getMedicos(){
    ArrayList <Medico> lista = new ArrayList<Medico>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMedicos}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Medico(resultado.getInt("codigoMedico"),
                resultado.getInt("licenciaMedica"),
                resultado.getString("nombres"),
                resultado.getString("apellidos"),
                resultado.getDate("horaEntrada"),
                resultado.getDate("horaSalida"),
                resultado.getString("sexo")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarMedicos = FXCollections.observableList(lista);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     public void ventanaTelefonoMedico(){
 escenarioPrincipal.ventanaTelefonoMedico();
 }
    
public void menuPrincipal (){
    escenarioPrincipal.menuPrincipal();
}
  public void ventanaEspecialidad(){
 escenarioPrincipal.ventanaEspecialidad();
 }
  public void ventanaMedicoEspecialidad(){
  escenarioPrincipal.ventanaMedicoEspecialidad();
  }
 public void seleccionar (){
    cmbCodigo.getSelectionModel().select(buscar(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
     txtLicencia.setText(String.valueOf(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
    txtNombre.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
    txtApellidos.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
    dtpHoraEntrada.selectedDateProperty().set(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
    dtpHoraSalida.selectedDateProperty().set(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
    txtSexo.setText(((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
    
    }
 public Medico buscar(int codigoMedico){
     Medico resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Medico (?)}");
         procedimiento.setInt(1,codigoMedico);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Medico (registro.getInt("codigoMedico"),
                                    registro.getInt("licenciaMedica"),
                                    registro.getString("nombres"),
                                    registro.getString("apellidos"),
                                    registro.getDate("horaEntrada"),
                                    registro.getDate("horaSalida"),
                                    registro.getString("sexo"));
         }
     }catch(Exception e){
         e.printStackTrace();
         e.getMessage();
     }
    return resultado;
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
                if (tblMedicos.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Medico ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicos (?)}");
                            procedimiento.setInt(1,((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listarMedicos.remove(tblMedicos.getSelectionModel().getSelectedIndex());
                            cargarDatos();
                            limpiar();
                            
                       }catch(Exception e){
                        e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                    break;
                }
        }    
 }
 public void editar (){
    switch (tipoOperaciones){
    
        case Ninguno:
            if(tblMedicos.getSelectionModel().getSelectedItem()!= null){
            btnAgregar.setText("Actualizarr");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReporte.setDisable(true);
            tipoOperaciones = operaciones.Actualizar;
           
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
                tipoOperaciones = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                
                cargarDatos();
                break;
    }else{  btnEditar.setText("✍");
                btnReporte.setText("✔");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperaciones = operaciones.Ninguno;
                     desactivar();
                     break;
            }
        }
    }
 public void actualizar (){
 try{
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarMedicos(?,?,?,?,?,?,?)}");
     Medico registro =(Medico) tblMedicos.getSelectionModel().getSelectedItem();
     registro.setCodigoMedico(Integer.parseInt(cmbCodigo.getSelectionModel().getSelectedItem().toString()));
     registro.setLicenciaMedica(Integer.parseInt(txtLicencia.getText()));
     registro.setNombres(txtNombre.getText());
     registro.setApellidos(txtApellidos.getText());
      procedimiento.setDate(4, (java.sql.Date)registro.getHoraEntrada());
     procedimiento.setDate(5, (java.sql.Date)registro.getHoraSalida());
     registro.setSexo(txtSexo.getText());
     
     procedimiento.setInt(1,registro.getCodigoMedico() );
     procedimiento.setInt(2, registro.getLicenciaMedica());
     procedimiento.setString(3, registro.getNombres());
     procedimiento.setString(4, registro.getApellidos());
     procedimiento.setDate(5, new java.sql.Date(registro.getHoraEntrada().getTime()));
     procedimiento.setDate(6, new java.sql.Date(registro.getHoraSalida().getTime()));
     procedimiento.setString(7, registro.getSexo());
     procedimiento.execute();
     
 
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
 public void limpiar(){
            txtLicencia.setText("");
            txtNombre.setText("");
            txtApellidos.setText("");
            txtSexo.setText("");
 }
 public void guardar() {   
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicencia.getText()));
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(new java.sql.Date(dtpHoraEntrada.getSelectedDate().getTime()));
        registro.setHoraSalida(new java.sql.Date(dtpHoraSalida.getSelectedDate().getTime()));
     registro.setSexo(txtSexo.getText());
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos (?,?,?,?,?,?)}");
     procedimiento.setInt(1, registro.getLicenciaMedica());
     procedimiento.setString(2, registro.getNombres());
     procedimiento.setString(3, registro.getApellidos());
     procedimiento.setDate(4, (java.sql.Date)registro.getHoraEntrada());
     procedimiento.setDate(5, (java.sql.Date)registro.getHoraSalida());
     procedimiento.setString(6, registro.getSexo());
     procedimiento.execute();
     listarMedicos.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
 public void cancelar(){
   btnAgregar.setText("✚");
   btnEliminar.setText("✖");
   btnEditar.setDisable(false);
   btnReporte.setDisable(false); 
 
 }   
 public void activar(){
          
          cmbCodigo.setDisable(false);
          txtLicencia.setDisable(false);
          txtNombre.setDisable(false);
          txtApellidos.setDisable(false);
          txtSexo.setDisable(false);
          dtpHoraEntrada.setDisable(false);
          dtpHoraSalida.setDisable(false);
          grpHoraSalida.setDisable(false);
          grpHoraEntrada.setDisable(false);
            
            cmbCodigo.setEditable(true);
            txtLicencia.setEditable(true);
            txtNombre.setEditable(true);
            txtApellidos.setEditable(true);
            txtSexo.setEditable(true);
          
        }      
 public void desactivar() {
         cmbCodigo.setDisable(true);
         txtLicencia.setDisable(true);
         txtNombre.setDisable(true);
         txtApellidos.setDisable(true);
         txtSexo.setDisable(true);
         dtpHoraEntrada.setDisable(true);
            dtpHoraSalida.setDisable(true);
            grpHoraSalida.setDisable(true);
            grpHoraEntrada.setDisable(true);
            
            cmbCodigo.setEditable(false);
                    txtLicencia.setEditable(false);
                        txtNombre.setEditable(false);
                     txtApellidos.setEditable(false);
                txtSexo.setEditable(false);
        
        
    }
 public void imprimirReporte(){
 if(tblMedicos.getSelectionModel().getSelectedItem()!= null){
 int codigoMedico = ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico();
    Map parametros = new HashMap();
    parametros.put("p_codigoMedico", codigoMedico);
    GenerarReporte.mostrarReporte("MedicoReport.jasper", "Reporte Medico", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
}

    
 
    

    
    
    
    
