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
import org.alvaroramirez.bean.Horarios;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class HorariosController implements Initializable{
     private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoOperaciones = operaciones.Ninguno;
 private ObservableList <Horarios> listarHorarios;
 
 private  Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
               cargarDatos();
        cmbCodigoHorario.setItems(getHorarios());
        
        dtpHorarioInicio = new DatePicker (Locale.ENGLISH);
        dtpHorarioInicio.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpHorarioInicio.getCalendarView().todayButtonTextProperty().set("Today");
        grpHorarioInicio.add(dtpHorarioInicio,0,0);
        
          
        dtpHorarioSalida = new DatePicker (Locale.ENGLISH);
        dtpHorarioSalida.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpHorarioSalida.getCalendarView().todayButtonTextProperty().set("Today");
        grpHorarioSalida.add(dtpHorarioSalida,0,0);
      
   
    }
     private DatePicker dtpHorarioSalida;
         private DatePicker dtpHorarioInicio;
         
        @FXML private ComboBox cmbCodigoHorario;
        
        @FXML private TextField txtLunes;
        @FXML private TextField txtMartes;
        @FXML private TextField txtMiercoles;
        @FXML private TextField txtJueves;
        @FXML private TextField txtViernes;
        
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private GridPane grpHorarioSalida;
        @FXML private GridPane grpHorarioInicio;
        
        @FXML private TableView tblHorarios;
        @FXML private TableColumn colCodigoHorarios;
        @FXML private TableColumn colInicio;
        @FXML private TableColumn colSalida;
        @FXML private TableColumn colLunes;
        @FXML private TableColumn colMartes;
        @FXML private TableColumn colMiercoles; 
        @FXML private TableColumn colJueves;
        @FXML private TableColumn colViernes;
     
    
       
    public void cargarDatos(){
        
        tblHorarios.setItems(getHorarios());
        colCodigoHorarios.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("codigoHorario"));
        colInicio.setCellValueFactory(new PropertyValueFactory <Horarios,Date>("horarioInicio"));
        colSalida.setCellValueFactory(new PropertyValueFactory <Horarios,Date>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("Miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory <Horarios,Integer>("viernes"));
     }
    
    public ObservableList <Horarios> getHorarios(){
    ArrayList <Horarios> lista = new ArrayList<Horarios>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarHorarios}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Horarios(resultado.getInt("codigoHorario"),
                resultado.getDate("horarioInicio"),
                resultado.getDate("horarioSalida"),
                resultado.getInt("lunes"),
                resultado.getInt("martes"),
                resultado.getInt("Miercoles"),
                resultado.getInt("jueves"),
                                resultado.getInt("viernes")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarHorarios = FXCollections.observableList(lista);
    }
    
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

 public void seleccionar (){
    cmbCodigoHorario.getSelectionModel().select(buscar(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario()));
     dtpHorarioInicio.selectedDateProperty().set(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
    dtpHorarioSalida.selectedDateProperty().set(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
    txtLunes.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getLunes()));
    txtMartes.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getMartes()));
    txtMiercoles.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles()));
    txtJueves.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getJueves()));
    txtViernes.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getViernes()));
    
    }
 public Horarios buscar(int codigoHorario){
     Horarios resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Horarios (?)}");
         procedimiento.setInt(1,codigoHorario);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Horarios (registro.getInt("codigoHorario"),
                registro.getDate("horarioInicio"),
                registro.getDate("horarioSalida"),
                registro.getInt("lunes"),
                registro.getInt("martes"),
                registro.getInt("Miercoles"),
                registro.getInt("jueves"),
                registro.getInt("viernes"));
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
                if (tblHorarios.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Horarios ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorarios (?)}");
                            procedimiento.setInt(1,((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                            procedimiento.execute();
                            listarHorarios.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
            if(tblHorarios.getSelectionModel().getSelectedItem()!= null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Horarios ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarHorarios(?,?,?,?,?,?,?,?)}");
     Horarios registro =(Horarios) tblHorarios.getSelectionModel().getSelectedItem();
     registro.setCodigoHorario(Integer.parseInt(cmbCodigoHorario.getSelectionModel().getSelectedItem().toString()));
     procedimiento.setDate(4, (java.sql.Date)registro.getHorarioInicio());
     procedimiento.setDate(5, (java.sql.Date)registro.getHorarioSalida());
     registro.setLunes(Integer.parseInt(txtLunes.getText()));
     registro.setMartes(Integer.parseInt(txtMartes.getText()));
     registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
     registro.setJueves(Integer.parseInt(txtJueves.getText()));
     registro.setViernes(Integer.parseInt(txtViernes.getText()));
     
     procedimiento.setInt(1, registro.getCodigoHorario());
     procedimiento.setDate(2, new java.sql.Date(registro.getHorarioInicio().getTime()));
     procedimiento.setDate(3, new java.sql.Date(registro.getHorarioSalida().getTime()));
     procedimiento.setInt(4, registro.getLunes());
     procedimiento.setInt(5, registro.getMartes());
     procedimiento.setInt(6, registro.getMiercoles());
     procedimiento.setInt(7, registro.getJueves());
     procedimiento.setInt(8, registro.getViernes());
     procedimiento.execute();
     
 
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
 public void activar(){
          
          cmbCodigoHorario.setDisable(false);
          txtLunes.setDisable(false);
          txtMartes.setDisable(false);
          txtMiercoles.setDisable(false);
          txtJueves.setDisable(false);
          txtViernes.setDisable(false);
          dtpHorarioInicio.setDisable(false);
          dtpHorarioSalida.setDisable(false);
          grpHorarioSalida.setDisable(false);
          grpHorarioInicio.setDisable(false);
            
            cmbCodigoHorario.setEditable(true);
            txtLunes.setEditable(true);
            txtMartes.setEditable(true);
            txtMiercoles.setEditable(true);
            txtJueves.setEditable(true);
            txtViernes.setEditable(true);
          
        }      
 public void limpiar(){
            txtLunes.setText("");
            txtMartes.setText("");
            txtMiercoles.setText("");
            txtJueves.setText("");
            txtViernes.setText("");
 }
    public void guardar() {   
        Horarios registro = new Horarios();
        registro.setHorarioInicio(new java.sql.Date(dtpHorarioInicio.getSelectedDate().getTime()));
        registro.setHorarioSalida(new java.sql.Date(dtpHorarioSalida.getSelectedDate().getTime()));
        
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorarios (?,?,?,?,?,?,?)}");
         procedimiento.setDate(1, (java.sql.Date)registro.getHorarioInicio());
     procedimiento.setDate(2, (java.sql.Date)registro.getHorarioSalida());
     procedimiento.setInt(3, registro.getLunes());
     procedimiento.setInt(4, registro.getMartes());
     procedimiento.setInt(5, registro.getMiercoles());
     procedimiento.setInt(6, registro.getJueves());
     procedimiento.setInt(7, registro.getViernes());
     
     procedimiento.execute();
     listarHorarios.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
 public void cancelar(){
 
 
 }   
    public void desactivar() {
             cmbCodigoHorario.setDisable(true);
          txtLunes.setDisable(true);
          txtMartes.setDisable(true);
          txtMiercoles.setDisable(true);
          txtJueves.setDisable(true);
          txtViernes.setDisable(true);
          dtpHorarioInicio.setDisable(true);
          dtpHorarioSalida.setDisable(true);
          grpHorarioSalida.setDisable(true);
          grpHorarioInicio.setDisable(true);
            
            cmbCodigoHorario.setEditable(false);
            txtLunes.setEditable(false);
            txtMartes.setEditable(false);
            txtMiercoles.setEditable(false);
            txtJueves.setEditable(false);
            txtViernes.setEditable(false);
        
    }   
           public void imprimirReporte(){
 if(tblHorarios.getSelectionModel().getSelectedItem()!= null){
 int codigoHorario = ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario();
    Map parametros = new HashMap();
    parametros.put("p_codigoHorario", codigoHorario);
    GenerarReporte.mostrarReporte("HorariosReport.jasper", "Reporte Horarios", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }    
}
