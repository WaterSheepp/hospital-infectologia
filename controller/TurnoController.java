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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alvaroramirez.bean.Turno;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class TurnoController implements Initializable{
    
 private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoOperaciones = operaciones.Ninguno;
 private ObservableList <Turno> listarTurnos;
 
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
        public void ventanaMedicoEspecialidad(){
  escenarioPrincipal.ventanaMedicoEspecialidad();
  }
 
 public void ventanaPacientes(){
 escenarioPrincipal.ventanaPacientes();
 }
 public void ventanaResponsableTurno (){
 escenarioPrincipal.ventanaResponsableTurno();
 }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
         cmbCodigoTurno.setItems(getTurno());
                 
        dtpFechaTurno = new DatePicker (Locale.ENGLISH);
        dtpFechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpFechaTurno.getCalendarView().todayButtonTextProperty().set("Today");
        grpFechaTurno.add(dtpFechaTurno,0,0);
        
          
        dtpFechaCita = new DatePicker (Locale.ENGLISH);
        dtpFechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpFechaCita.getCalendarView().todayButtonTextProperty().set("Today");
        grpFechaCita.add(dtpFechaCita,0,0);
    }
      private DatePicker dtpFechaTurno;
         private DatePicker dtpFechaCita;
        @FXML private ComboBox cmbCodigoTurno;
        @FXML private TextField txtCodigoMedicoEspecialidad;
        @FXML private TextField txtCodigoResponsableTurno;
        @FXML private TextField txtCodigoPaciente;
        @FXML private TextField txtValorCita;
       
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        
        @FXML private GridPane grpFechaTurno;
        @FXML private GridPane grpFechaCita;
        @FXML private TableView tblTurno;
        @FXML private TableColumn colCodigoTurno;
        @FXML private TableColumn colFechaTurno;
        @FXML private TableColumn colFechaCita;
        @FXML private TableColumn colValorCita;
        @FXML private TableColumn colCodigoMedicoEspecialidad;
        @FXML private TableColumn colResponsableTurno; 
        @FXML private TableColumn colCodigoPaciente;
  
   public void cargarDatos(){
        tblTurno.setItems(getTurno());
        colCodigoTurno.setCellValueFactory(new PropertyValueFactory <Turno,Integer>("codigoTurno"));
        colFechaTurno.setCellValueFactory(new PropertyValueFactory <Turno,Date>("fechaTurno"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory <Turno,Date>("fechaCita"));
        colValorCita.setCellValueFactory(new PropertyValueFactory <Turno,Double>("valorCita"));
        colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory <Turno,Integer>("codigoMedicoEspecialidad"));
        colResponsableTurno.setCellValueFactory(new PropertyValueFactory <Turno,Integer>("codigoResponsableTurno"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory <Turno,Integer>("codigoPaciente"));
     }
                public ObservableList <Turno> getTurno(){
    ArrayList <Turno> lista = new ArrayList<Turno>();
    
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarTurno}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Turno(resultado.getInt("codigoTurno"),
                resultado.getDate("fechaTurno"),
                resultado.getDate("fechaCita"),
                resultado.getDouble("valorCita"),
                resultado.getInt("codigoMedicoEspecialidad"),
                resultado.getInt("codigoResponsableTurno"),
                resultado.getInt("codigoPaciente")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarTurnos = FXCollections.observableList(lista);
    }
     public void seleccionar (){
    cmbCodigoTurno.getSelectionModel().select(buscar(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno()));
    txtCodigoMedicoEspecialidad.setText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad()));
    txtCodigoResponsableTurno.setText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
    txtCodigoPaciente.setText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
     txtValorCita.setText(String.valueOf(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getValorCita()));
    dtpFechaTurno.selectedDateProperty().set(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getFechaTurno());
    dtpFechaCita.selectedDateProperty().set(((Turno) tblTurno.getSelectionModel().getSelectedItem()).getFechaCita());
    
    }
 public Turno buscar(int codigoTurno){
     Turno resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Turno(?)}");
         procedimiento.setInt(1,codigoTurno);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Turno (registro.getInt("codigoTurno"),
                registro.getDate("fechaTurno"),
                registro.getDate("fechaCita"),
                registro.getDouble("valorCita"),
                registro.getInt("codigoMedicoEspecialidad"),
                registro.getInt("codigoResponsableTurno"),
                registro.getInt("codigoPaciente"));
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
                if (tblTurno.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Turno ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurno (?)}");
                            procedimiento.setInt(1,((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno());
                            procedimiento.execute();
                            listarTurnos.remove(tblTurno.getSelectionModel().getSelectedIndex());
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
            if(tblTurno.getSelectionModel().getSelectedItem()!= null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Turno ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarTurno(?,?,?,?,?,?,?)}");
     Turno registro =(Turno) tblTurno.getSelectionModel().getSelectedItem();
     registro.setCodigoTurno(Integer.parseInt(cmbCodigoTurno.getSelectionModel().getSelectedItem().toString()));
     registro.setCodigoMedicoEspecialidad(Integer.parseInt(txtCodigoMedicoEspecialidad.getText()));
     registro.setCodigoResponsableTurno(Integer.parseInt(txtCodigoResponsableTurno.getText()));
     registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
     
     registro.setValorCita(Double.parseDouble(txtValorCita.getText()));
     procedimiento.setDate(4, (java.sql.Date)registro.getFechaTurno());
     procedimiento.setDate(5, (java.sql.Date)registro.getFechaCita());
     
     procedimiento.setInt(1,registro.getCodigoTurno());
     procedimiento.setDate(2, new java.sql.Date(registro.getFechaTurno().getTime()));
     procedimiento.setDate(3, new java.sql.Date(registro.getFechaCita().getTime()));
     procedimiento.setDouble(4, registro.getValorCita());
     procedimiento.setInt(5, registro.getCodigoMedicoEspecialidad());
     procedimiento.setInt(6, registro.getCodigoResponsableTurno());
     procedimiento.setInt(7, registro.getCodigoPaciente());
     
     procedimiento.execute();
     
 
 
 }catch(SQLException e){
     e.printStackTrace();
     }
 }
      public void activar(){
          
          cmbCodigoTurno.setDisable(false);
          txtCodigoMedicoEspecialidad.setDisable(false);
          txtCodigoResponsableTurno.setDisable(false);
          txtCodigoPaciente.setDisable(false);
          txtValorCita.setDisable(false);
          dtpFechaCita.setDisable(false);
          dtpFechaTurno.setDisable(false);
          grpFechaTurno.setDisable(false);
          grpFechaCita.setDisable(false);
            
            cmbCodigoTurno.setEditable(true);
            txtCodigoMedicoEspecialidad.setEditable(true);
            txtCodigoResponsableTurno.setEditable(true);
            txtCodigoPaciente.setEditable(true);
            txtValorCita.setEditable(true);
        }      
        public void desactivar() {
        cmbCodigoTurno.setDisable(true);
          txtCodigoMedicoEspecialidad.setDisable(true);
          txtCodigoResponsableTurno.setDisable(true);
          txtCodigoPaciente.setDisable(true);
          
          txtValorCita.setDisable(true);
          dtpFechaCita.setDisable(true);
          dtpFechaTurno.setDisable(true);
          grpFechaTurno.setDisable(true);
          grpFechaCita.setDisable(true);
            
             cmbCodigoTurno.setEditable(false);
            txtCodigoMedicoEspecialidad.setEditable(false);
            txtCodigoResponsableTurno.setEditable(false);
            txtCodigoPaciente.setEditable(false);
            txtValorCita.setEditable(false);
        }      
        
        
    
 public void limpiar(){
     txtCodigoMedicoEspecialidad.setText("");
             txtCodigoResponsableTurno.setText("");
             txtCodigoPaciente.setText("");
            txtValorCita.setText("");
 }
         public void guardar() {   
        Turno registro = new Turno();
        registro.setValorCita(Double.parseDouble(txtValorCita.getText()));
        registro.setCodigoMedicoEspecialidad(Integer.parseInt(txtCodigoMedicoEspecialidad.getText()));
        registro.setCodigoResponsableTurno(Integer.parseInt(txtCodigoResponsableTurno.getText()));
        registro.setCodigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
        registro.setFechaTurno(new java.sql.Date(dtpFechaTurno.getSelectedDate().getTime()));
        registro.setFechaCita(new java.sql.Date(dtpFechaCita.getSelectedDate().getTime()));
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno (?,?,?,?,?,?)}");
     procedimiento.setDate(1, (java.sql.Date)registro.getFechaTurno());
     procedimiento.setDate(2, (java.sql.Date)registro.getFechaCita());
     procedimiento.setDouble(3, registro.getValorCita());
     procedimiento.setInt(4, registro.getCodigoMedicoEspecialidad());
     procedimiento.setInt(5, registro.getCodigoResponsableTurno());
     procedimiento.setInt(6, registro.getCodigoPaciente());
     procedimiento.execute();
     listarTurnos.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
          public void imprimirReporte(){
 if(tblTurno.getSelectionModel().getSelectedItem()!= null){
 int codigoTurno = ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodigoTurno();
    Map parametros = new HashMap();
    parametros.put("p_codigoTurno", codigoTurno);
    GenerarReporte.mostrarReporte("TurnoReport.jasper", "Reporte Turno", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
         
                
}
