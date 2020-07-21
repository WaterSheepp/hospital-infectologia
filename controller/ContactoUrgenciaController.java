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
import org.alvaroramirez.bean.ContactoUrgencia;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class  ContactoUrgenciaController implements Initializable{

 private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoOperaciones = operaciones.Ninguno;
 private ObservableList <ContactoUrgencia> listarContactoUrgencia;
 
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
     public void ventanaPacientes(){
 escenarioPrincipal.ventanaPacientes();
 }
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoContacto.setItems(getContactoUrgencia());
    }
    
    @FXML private ComboBox cmbCodigoContacto;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumero;
    @FXML private TextField txtCodigoPaciente;
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblContacto;
        @FXML private TableColumn colCodigoContacto;
        @FXML private TableColumn colNombres;
        @FXML private TableColumn colApellidos;
        @FXML private TableColumn colNumero;
        @FXML private TableColumn colCodigoPaciente;
        
    public void cargarDatos(){
        
        tblContacto.setItems(getContactoUrgencia());
            colCodigoContacto.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia,Integer>("codigoContactoUrgencia"));
                colNombres.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia,String>("nombres"));
                 colApellidos.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia,String>("apellidos"));
            colNumero.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia,String>("numeroContacto"));
       colCodigoPaciente.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia,String>("pacientes_codigoPaciente"));
     }
    
    public ObservableList <ContactoUrgencia> getContactoUrgencia(){
    ArrayList <ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarContactoUrgencia}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                resultado.getString("nombres"),
                resultado.getString("apellidos"),
                resultado.getString("numeroContacto"),
                resultado.getInt("pacientes_codigoPaciente")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarContactoUrgencia = FXCollections.observableList(lista);
    }

 public void seleccionar (){
    cmbCodigoContacto.getSelectionModel().select(buscar(((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia()));
     txtCodigoPaciente.setText(String.valueOf(((ContactoUrgencia) tblContacto.getSelectionModel().getSelectedItem()).getPacientes_codigoPaciente()));
    txtNombres.setText(((ContactoUrgencia) tblContacto.getSelectionModel().getSelectedItem()).getNombres());
    txtApellidos.setText(((ContactoUrgencia) tblContacto.getSelectionModel().getSelectedItem()).getApellidos());
    txtNumero.setText(((ContactoUrgencia) tblContacto.getSelectionModel().getSelectedItem()).getNumeroContacto());
    txtCodigoPaciente.setText(String.valueOf(((ContactoUrgencia) tblContacto.getSelectionModel().getSelectedItem()).getPacientes_codigoPaciente()));
    
    }
 public ContactoUrgencia buscar(int codigoContactoUrgencia){
     ContactoUrgencia resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_ContactoUrgencias(?)}");
         procedimiento.setInt(1,codigoContactoUrgencia);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new ContactoUrgencia (registro.getInt("codigoContactoUrgencia"),
                                    registro.getString("nombres"),
                                    registro.getString("apellidos"),
                                    registro.getString("numeroContacto"),
                                    registro.getInt("pacientes_codigoPaciente"));
         }
     
     }catch(Exception e){
         e.printStackTrace();
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
                if (tblContacto.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Telefono ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencias (?)}");
                            procedimiento.setInt(1,((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                            procedimiento.execute();
                            listarContactoUrgencia.remove(tblContacto.getSelectionModel().getSelectedIndex());
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
     public void editar (){
    switch (tipoOperaciones){
    
        case Ninguno:
            if(tblContacto.getSelectionModel().getSelectedItem()!= null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Paciente ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                actualizar();
                
                limpiar();
           btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoOperaciones = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                desactivar();
                break;
    }else{     btnEditar.setText("✍");
                btnReporte.setText("✔");
                tipoOperaciones = operaciones.Ninguno;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                     desactivar();
            }
        }
    }
     
      public void actualizar (){
 try{
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarContactoUrgencias(?,?,?,?,?)}");
     ContactoUrgencia registro =(ContactoUrgencia) tblContacto.getSelectionModel().getSelectedItem();
     registro.setCodigoContactoUrgencia(Integer.parseInt(cmbCodigoContacto.getSelectionModel().getSelectedItem().toString()));
 registro.setNombres((txtNombres.getText()));
        registro.setApellidos(txtApellidos.getText());
     registro.setNumeroContacto(txtNumero.getText());
     registro.setPacientes_codigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
     
     procedimiento.setInt(1,registro.getCodigoContactoUrgencia());
     procedimiento.setString(2, registro.getNombres());
     procedimiento.setString(3, registro.getApellidos());
     procedimiento.setString(4, registro.getNumeroContacto());
     procedimiento.setInt(5, registro.getPacientes_codigoPaciente());
     procedimiento.execute();
     
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
        public void activar(){
          cmbCodigoContacto.setDisable(false);
          txtNombres.setDisable(false);
          txtApellidos.setDisable(false);
          txtNumero.setDisable(false);
          txtCodigoPaciente.setDisable(false);
          
          cmbCodigoContacto.setEditable(true);
            txtNombres.setEditable(true);
            txtApellidos.setEditable(true);
            txtNumero.setEditable(true);
            txtCodigoPaciente.setEditable(true);
          
        }      
        public void limpiar(){
            txtNombres.setText("");
                txtApellidos.setText("");
                txtNumero.setText("");
            txtCodigoPaciente.setText("");
 }
   
         public void guardar() {   
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres((txtNombres.getText()));
        registro.setApellidos(txtApellidos.getText());
     registro.setNumeroContacto(txtNumero.getText());
     registro.setPacientes_codigoPaciente(Integer.parseInt(txtCodigoPaciente.getText()));
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencias (?,?,?,?)}");
     procedimiento.setString(1, registro.getNombres());
     procedimiento.setString(2, registro.getApellidos());
     procedimiento.setString(3, registro.getNumeroContacto());
     procedimiento.setInt(4, registro.getPacientes_codigoPaciente());
     procedimiento.execute();
     listarContactoUrgencia.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
    public void desactivar() {
        cmbCodigoContacto.setDisable(true);
       txtNombres.setDisable(true);
          txtApellidos.setDisable(true);
          txtNumero.setDisable(true);
          txtCodigoPaciente.setDisable(true);
            
          cmbCodigoContacto.setEditable(false);
            txtNombres.setEditable(false);
            txtApellidos.setEditable(false);
            txtNumero.setEditable(false);
            txtCodigoPaciente.setEditable(false);
    }
      public void imprimirReporte(){
 if(tblContacto.getSelectionModel().getSelectedItem()!= null){
 int codigoContactoUrgencia = ((ContactoUrgencia)tblContacto.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia();
    Map parametros = new HashMap();
    parametros.put("p_codigoContactoUrgencia", codigoContactoUrgencia);
    GenerarReporte.mostrarReporte("ContactoUrgenciareport.jasper", "Reporte Contacto", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }    
}

    
 
    

    
    
    
    
