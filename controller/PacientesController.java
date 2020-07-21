
package org.alvaroramirez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alvaroramirez.bean.Pacientes;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class PacientesController implements Initializable {
      private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoDeOperacion = operaciones.Ninguno;
 private ObservableList <Pacientes> listarPacientes;
 
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
     public void ventanaContactoUrgencias(){
 escenarioPrincipal.ventanaContactoUrgencia();
 }
        public void ventanaTurno(){
 escenarioPrincipal.ventanaTurno();
 }
 
 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        
        
        dtpFechaNacimiento = new DatePicker (Locale.ENGLISH);
        dtpFechaNacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpFechaNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        grpFechaNacimiento.add(dtpFechaNacimiento,0,0);
        
      
    }
    
        private DatePicker dtpFechaNacimiento;
        @FXML private TextField txtCodigoPaciente;
        @FXML private TextField txtDpi;
        @FXML private TextField txtNombres;
        @FXML private TextField txtApellidos;
        @FXML private TextField txtSexo;
        @FXML private TextField txtEdad;
        @FXML private TextField txtOcupacion;
        @FXML private TextField txtDireccion;
        @FXML private GridPane  grpFechaNacimiento;
        @FXML private Button btnNuevo;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblPacientes;
        @FXML private TableColumn colCodigoPacientes;
        @FXML private TableColumn colDpi;
        @FXML private TableColumn colNombres;
        @FXML private TableColumn colApellidos;
        @FXML private TableColumn colFechaNacimiento;
        @FXML private TableColumn colEdad;
        @FXML private TableColumn colDireccion;
        @FXML private TableColumn colOcupacion;
        @FXML private TableColumn colSexo;
        
         public void cargarDatos(){
        
        tblPacientes.setItems(getPacientes());
            colCodigoPacientes.setCellValueFactory(new PropertyValueFactory <Pacientes,Integer>("codigoPaciente"));
            colDpi.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("DPI"));
            colApellidos.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("apellidos"));
            colNombres.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("nombres"));
            colFechaNacimiento.setCellValueFactory(new PropertyValueFactory <Pacientes,Date>("fechaNacimiento"));
            colEdad.setCellValueFactory(new PropertyValueFactory <Pacientes,Integer>("edad"));
            colDireccion.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("direccion"));
            colOcupacion.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("ocupacion"));
            colSexo.setCellValueFactory(new PropertyValueFactory <Pacientes,String>("sexo"));
     }
      public ObservableList <Pacientes> getPacientes(){
          
    ArrayList <Pacientes> lista = new ArrayList<Pacientes>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarPacientes}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Pacientes(resultado.getInt("codigoPaciente"),
                resultado.getString("DPI"),
                resultado.getString("apellidos"),
                resultado.getString("nombres"),
                resultado.getDate("fechaNacimiento"),
                resultado.getInt("edad"),
                resultado.getString("direccion"),
                resultado.getString("ocupacion"),
                resultado.getString("sexo")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarPacientes = FXCollections.observableList(lista);
    }   
public void seleccionar (){
    txtCodigoPaciente.setText(String.valueOf(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
    txtDpi.setText(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getDPI());
    txtApellidos.setText(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
    txtNombres.setText(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
    dtpFechaNacimiento.selectedDateProperty().set(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
    txtEdad.setText(String.valueOf(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
    txtDireccion.setText(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
    txtOcupacion.setText(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
    txtSexo.setText(((Pacientes) tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
    
    }
public Pacientes buscar(int codigoPaciente){
     Pacientes resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Pacientes (?)}");
         procedimiento.setInt(1,codigoPaciente);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Pacientes (registro.getInt("codigoPaciente"),
                registro.getString("DPI"),
                registro.getString("apellidos"),
                registro.getString("nombres"),
                registro.getDate("fechaNacimiento"),
                registro.getInt("edad"),
                registro.getString("direccion"),
                registro.getString("ocupacion"),
                registro.getString("sexo"));
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
  
                   btnNuevo.setText("✚");
                   btnEliminar.setText("✖");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                    tipoDeOperacion = operaciones.Nuevo;
                break;
            default:
             if (tblPacientes.getSelectionModel().getSelectedItem()!= null){
             int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Paciente ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
              if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPacientes (?)}");
                            procedimiento.setInt(1,((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            listarPacientes.remove(tblPacientes.getSelectionModel().getSelectedIndex());
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
    switch (tipoDeOperacion){
    
        case Ninguno:
            if(tblPacientes.getSelectionModel().getSelectedItem()!= null){
            btnEditar.setText("Actualizar");
            btnReporte.setText("Cancelar");
            tipoDeOperacion = operaciones.Actualizar;
            btnNuevo.setDisable(true);
            btnEliminar.setDisable(true);
            activar();
                
            break;
            }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
            break;
            case Actualizar:
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Medico ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                
                actualizar();
                
                limpiar();
                btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoDeOperacion = operaciones.Ninguno;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                desactivar();
                
                cargarDatos();
                break;
    }else {
                    btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoDeOperacion = operaciones.Ninguno;
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                     desactivar();
         
                 }
    }
    }
     
 public void actualizar (){
 try{
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarPacientes(?,?,?,?,?,?,?,?,?)}");
     Pacientes registro =(Pacientes) tblPacientes.getSelectionModel().getSelectedItem();
    registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
    registro.setDPI(txtDpi.getText());
        registro.setEdad(Integer.parseInt(txtEdad.getText()));
        
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setFechaNacimiento(new java.sql.Date(dtpFechaNacimiento.getSelectedDate().getTime()));
        registro.setSexo(txtSexo.getText());
     
     procedimiento.setInt(1,registro.getCodigoPaciente());
     procedimiento.setString(2, registro.getDPI());
     procedimiento.setString(3, registro.getApellidos());
     procedimiento.setString(4, registro.getNombres());
     procedimiento.setDate(5, new java.sql.Date(registro.getFechaNacimiento().getTime()));
     procedimiento.setInt(6, registro.getEdad());
     procedimiento.setString(7, registro.getDireccion());
     procedimiento.setString(8, registro.getOcupacion());
     procedimiento.setString(9, registro.getSexo());
     procedimiento.execute();
     
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
    public  void nuevo(){
     
     switch (tipoDeOperacion){
         case Ninguno:
             
             activar();
             btnNuevo.setText("Guardar");
             btnEliminar.setText("Cancelar");
             btnEditar.setDisable(true);
             btnReporte.setDisable(true);
            
              tipoDeOperacion = operaciones.Guardar  ;
         break;
         
         case Guardar:
             guardar();
              desactivar();
                limpiar();
                   btnNuevo.setText("✚");
                btnEliminar.setText("✖");
                  btnEditar.setDisable(false);
             btnReporte.setDisable(false); 
                  
                tipoDeOperacion = operaciones.Ninguno;
             cargarDatos();
             break;
  }
 }
     public void activar(){
         
          txtDpi.setDisable(false);
          txtNombres.setDisable(false);
          txtApellidos.setDisable(false);
          txtOcupacion.setDisable(false);
          txtDireccion.setDisable(false);
          txtEdad.setDisable(false);
          txtSexo.setDisable(false);
          dtpFechaNacimiento.setDisable(false);
          
          grpFechaNacimiento.setDisable(false);
            
            txtDpi.setEditable(true);
            txtNombres.setEditable(true);
            txtOcupacion.setEditable(true);
            txtApellidos.setEditable(true);
            txtSexo.setEditable(true);
            txtEdad.setEditable(true);
            txtDireccion.setEditable(true);
        }    
         public void desactivar() {
          txtDpi.setDisable(true);
          txtNombres.setDisable(true);
          txtApellidos.setDisable(true);
          txtOcupacion.setDisable(true);
          txtDireccion.setDisable(true);
          txtEdad.setDisable(true);
          txtSexo.setDisable(true);
          dtpFechaNacimiento.setDisable(true);
          
          grpFechaNacimiento.setDisable(true);
            
            txtDpi.setEditable(false);
            txtNombres.setEditable(false);
            txtOcupacion.setEditable(false);
            txtApellidos.setEditable(false);
            txtSexo.setEditable(false);
            txtEdad.setEditable(false);
            txtDireccion.setEditable(false);
        
    }
     public void limpiar(){
            txtDpi.setText("");
                txtNombres.setText("");
                txtApellidos.setText("");
                txtDireccion.setText("");
                txtOcupacion.setText("");
                txtEdad.setText("");
            txtSexo.setText("");
}
              public void guardar() {   
        Pacientes registro = new Pacientes();
        registro.setDPI(txtDpi.getText());
        registro.setEdad(Integer.parseInt(txtEdad.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setFechaNacimiento(new java.sql.Date(dtpFechaNacimiento.getSelectedDate().getTime()));
        registro.setSexo(txtSexo.getText());
         
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPacientes (?,?,?,?,?,?,?,?)}");
     procedimiento.setString(1, registro.getDPI());
     procedimiento.setString(2, registro.getApellidos());
     procedimiento.setString(3, registro.getNombres());
     procedimiento.setDate(4,(java.sql.Date)registro.getFechaNacimiento());
     procedimiento.setInt(5, registro.getEdad());
     procedimiento.setString(6, registro.getDireccion());
     procedimiento.setString(7, registro.getOcupacion());
     procedimiento.setString(8, registro.getSexo());
     procedimiento.execute();
     listarPacientes.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
           public void imprimirReporte(){
 if(tblPacientes.getSelectionModel().getSelectedItem()!= null){
 int codigoPaciente = ((Pacientes)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente();
    Map parametros = new HashMap();
    parametros.put("p_codigoPaciente", codigoPaciente);
    GenerarReporte.mostrarReporte("Pacientesreport.jasper", "Reporte Pacientes", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }      
}

    
