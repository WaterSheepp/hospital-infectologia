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
import org.alvaroramirez.bean.Especialidad;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class EspecialidadController implements Initializable{
    private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoDeOperacion = operaciones.Ninguno;
 private ObservableList <Especialidad> listarEspecialidad;
 
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
    public void ventanaEspecialidad(){
 escenarioPrincipal.ventanaEspecialidad();
 }
     public void ventanaMedicos(){
 escenarioPrincipal.ventanaMedicos();
 }
      public void ventanaTelefonoMedico(){
 escenarioPrincipal.ventanaTelefonoMedico();
 }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cargarDatos();
        cmbCodigoEspecialidad.setItems(getEspecialidad());  
    }
        @FXML private ComboBox cmbCodigoEspecialidad;
        @FXML private TextField txtNombreEspecialidad;      
        @FXML private Button btnAgregar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblEspecialidad;
        @FXML private TableColumn colCodigoEspecialidad;
        @FXML private TableColumn colNombreEspecialidad;
        
public void cargarDatos(){
        
        tblEspecialidad.setItems(getEspecialidad());
            colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad,Integer>("codigoEspecialidad"));
                colNombreEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad,String>("nombreEspecialidad"));
     }
 public ObservableList <Especialidad> getEspecialidad(){
    ArrayList <Especialidad> lista = new ArrayList<Especialidad>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarEspecialidades}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                resultado.getString("nombreEspecialidad")));
               
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarEspecialidad = FXCollections.observableList(lista);
    }
        public void seleccionar (){
    cmbCodigoEspecialidad.getSelectionModel().select(buscar(((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
    txtNombreEspecialidad.setText((((Especialidad) tblEspecialidad.getSelectionModel().getSelectedItem()).getNombreEspecialidad()));
    }
             public Especialidad buscar(int codigoEspecialidad){
     Especialidad resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_Especialidades(?)}");
         procedimiento.setInt(1,codigoEspecialidad);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new Especialidad (registro.getInt("codigoEspecialidad"),
                                            registro.getString("nombreEspecialidad"));
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
                if (tblEspecialidad.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar Especialidad ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidades (?)}");
                            procedimiento.setInt(1,((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                            procedimiento.execute();
                            listarEspecialidad.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
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
            if(tblEspecialidad.getSelectionModel().getSelectedItem()!= null){
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
                int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de Editar el registro ?"," Editar Especialidad ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarEspecialidades(?,?)}");
     Especialidad registro =(Especialidad) tblEspecialidad.getSelectionModel().getSelectedItem();
     registro.setCodigoEspecialidad(Integer.parseInt(cmbCodigoEspecialidad.getSelectionModel().getSelectedItem().toString()));
     registro.setNombreEspecialidad((txtNombreEspecialidad.getText()));
     
     procedimiento.setInt(1,registro.getCodigoEspecialidad());
     procedimiento.setString(2, registro.getNombreEspecialidad());
     procedimiento.execute();
 
 }catch(SQLException e){
     e.printStackTrace();
 }
 
 }
  
         public void limpiar(){
            txtNombreEspecialidad.setText("");
 }
          public void guardar() {   
        Especialidad registro = new Especialidad();
        registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidades (?)}");
     procedimiento.setString(1, registro.getNombreEspecialidad());
     procedimiento.execute();
     listarEspecialidad.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
      public void activar(){
          cmbCodigoEspecialidad.setDisable(false);
          txtNombreEspecialidad.setDisable(false);
    
          cmbCodigoEspecialidad.setEditable(true);
          txtNombreEspecialidad.setEditable(true);
        }  
          public void desactivar() {
       cmbCodigoEspecialidad.setDisable(true);
       txtNombreEspecialidad.setDisable(true);
       
       cmbCodigoEspecialidad.setEditable(false);
       txtNombreEspecialidad.setEditable(false);
    }
          public void imprimirReporte(){
 if(tblEspecialidad.getSelectionModel().getSelectedItem()!= null){
 int codigoEspecialidad = ((Especialidad)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad();
    Map parametros = new HashMap();
    parametros.put("p_codigoEspecialidad", codigoEspecialidad);
    GenerarReporte.mostrarReporte("EspecialidadesReporte.jasper", "Reporte Especialidad", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }   
    
}
