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

import org.alvaroramirez.bean.ResponsableTurno;
import org.alvaroramirez.db.Conexion;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;

public class ResponsableTurnoController implements Initializable{
      private enum operaciones {Nuevo,Guardar,Editar,Actualizar,Cancelar,Eliminar,Ninguno }
 private operaciones tipoDeOperacion = operaciones.Ninguno;
 private ObservableList <ResponsableTurno> listarResponsableTurno;
 
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
          public void ventanaCargo(){
  escenarioPrincipal.ventanaCargo();
  }
   public void ventanaTurno(){
 escenarioPrincipal.ventanaTurno();
 }
    public void ventanaAreas(){
 escenarioPrincipal.ventanaAreas();
 }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    @FXML private ComboBox cmbCodigoResponsableTurno;    
    @FXML private TextField txtNombreResponsable;
        @FXML private TextField txtApellidosResponsable;
        @FXML private TextField txtTelefonoPersonal;
        @FXML private TextField txtCodigoArea;
        @FXML private TextField txtCodigoCargo;
        @FXML private Button btnNuevo;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        @FXML private Button btnEditar;
        @FXML private TableView tblResponsableTurno;
        @FXML private TableColumn colCodigoResponsableTurno;
        @FXML private TableColumn colNombre;
        @FXML private TableColumn colApellidos;
        @FXML private TableColumn colTelefono;
        @FXML private TableColumn colCodigoArea;
        @FXML private TableColumn colCodigoCargo;
        
        public void cargarDatos(){
        
        tblResponsableTurno.setItems(getPacientes());
            colCodigoResponsableTurno.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,Integer>("codigoResponsableTurno"));
            colNombre.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,String>("nombreResponsable"));
            colApellidos.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,String>("apellidosResponsable"));
            colTelefono.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,String>("telefonoPersonal"));
            colCodigoArea.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,Integer>("codigoArea"));
            colCodigoCargo.setCellValueFactory(new PropertyValueFactory <ResponsableTurno,Integer>("codigoCargo"));
     }
      public ObservableList <ResponsableTurno> getPacientes(){
          
    ArrayList <ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{ PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarResponsableTurno}");
             ResultSet resultado = procedimiento.executeQuery();
        
    while(resultado.next()){
        lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                resultado.getString("nombreResponsable"),
                resultado.getString("apellidosResponsable"),
                resultado.getString("telefonoPersonal"),
                resultado.getInt("codigoArea"),
                resultado.getInt("codigoCargo")));
        }
    }catch(SQLException e){
    e.printStackTrace();
            }
   return listarResponsableTurno = FXCollections.observableList(lista);
    }   
public void seleccionar (){
    cmbCodigoResponsableTurno.getSelectionModel().select(buscar(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno()));
    txtNombreResponsable.setText(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable());
    txtApellidosResponsable.setText(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
    txtTelefonoPersonal.setText(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
    txtCodigoArea.setText(String.valueOf(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoArea()));
    txtCodigoCargo.setText(String.valueOf(((ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoCargo()));    
    
    }
public ResponsableTurno buscar(int codigoResponsableTurno){
     ResponsableTurno resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_Buscar_ResponsableTurno (?)}");
         procedimiento.setInt(1,codigoResponsableTurno);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
             resultado = new ResponsableTurno (registro.getInt("codigoResponsableTurno"),
                registro.getString("nombreResponsable"),
                registro.getString("apellidosResponsable"),
                registro.getString("telefonoPersonal"),
                registro.getInt("codigoArea"),
                     registro.getInt("codigoCargo"));
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
             if (tblResponsableTurno.getSelectionModel().getSelectedItem()!= null){
             int respuesta = JOptionPane.showConfirmDialog(null,"¿ Está seguro de eliminar el registro ?"," Eliminar ResponsableTurno ",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
              if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno (?)}");
                            procedimiento.setInt(1,((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                            procedimiento.execute();
                            listarResponsableTurno.remove(tblResponsableTurno.getSelectionModel().getSelectedIndex());
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
            if(tblResponsableTurno.getSelectionModel().getSelectedItem()!= null){
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
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarResponsableTurno(?,?,?,?,?,?)}");
     ResponsableTurno registro =(ResponsableTurno) tblResponsableTurno.getSelectionModel().getSelectedItem();
     registro.setCodigoResponsableTurno(Integer.parseInt(cmbCodigoResponsableTurno.getSelectionModel().getSelectedItem().toString()));
     registro.setApellidosResponsable(txtNombreResponsable.getText());
     registro.setApellidosResponsable(txtApellidosResponsable.getText());
     registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
     registro.setCodigoArea(Integer.parseInt(txtCodigoArea.getText()));
     registro.setCodigoCargo(Integer.parseInt(txtCodigoCargo.getText()));
     
     procedimiento.setInt(1,registro.getCodigoResponsableTurno());
     procedimiento.setString(2, registro.getNombreResponsable());
     procedimiento.setString(3, registro.getApellidosResponsable());
     procedimiento.setString(4, registro.getTelefonoPersonal());
     procedimiento.setInt(5, registro.getCodigoArea());
     procedimiento.setInt(6, registro.getCodigoCargo());     
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
         
          cmbCodigoResponsableTurno.setDisable(false);
          txtNombreResponsable.setDisable(false);
          txtApellidosResponsable.setDisable(false);
          txtTelefonoPersonal.setDisable(false);
          txtCodigoArea.setDisable(false);
          txtCodigoCargo.setDisable(false);
          
            cmbCodigoResponsableTurno.setEditable(true);
            txtNombreResponsable.setEditable(true);
            txtApellidosResponsable.setEditable(true);
            txtTelefonoPersonal.setEditable(true);
            txtCodigoArea.setEditable(true);
            txtCodigoCargo.setEditable(true);
        }    
         public void desactivar() {
          cmbCodigoResponsableTurno.setDisable(true);
          txtNombreResponsable.setDisable(true);
          txtApellidosResponsable.setDisable(true);
          txtTelefonoPersonal.setDisable(true);
          txtCodigoArea.setDisable(true);
          txtCodigoCargo.setDisable(true);
            
             cmbCodigoResponsableTurno.setEditable(false);
            txtNombreResponsable.setEditable(false);
            txtApellidosResponsable.setEditable(false);
            txtTelefonoPersonal.setEditable(false);
            txtCodigoArea.setEditable(false);
            txtCodigoCargo.setEditable(false);
        
    }
     public void limpiar(){
            txtNombreResponsable.setText("");
                txtApellidosResponsable.setText("");
                txtTelefonoPersonal.setText("");
                txtCodigoArea.setText("");
                txtCodigoCargo.setText("");
}
              public void guardar() {   
        ResponsableTurno registro = new ResponsableTurno();
         registro.setNombreResponsable(txtNombreResponsable.getText());
     registro.setApellidosResponsable(txtApellidosResponsable.getText());
     registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
     registro.setCodigoArea(Integer.parseInt(txtCodigoArea.getText()));
     registro.setCodigoCargo(Integer.parseInt(txtCodigoCargo.getText()));
         
     try {
     PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno (?,?,?,?,?)}");
    procedimiento.setString(1, registro.getNombreResponsable());
     procedimiento.setString(2, registro.getApellidosResponsable());
     procedimiento.setString(3, registro.getTelefonoPersonal());
     procedimiento.setInt(4, registro.getCodigoArea());
     procedimiento.setInt(5, registro.getCodigoCargo());     
     procedimiento.execute();
     listarResponsableTurno.add(registro);
     
     }catch (SQLException e){
     e.printStackTrace();
     }
}
                   public void imprimirReporte(){
 if(tblResponsableTurno.getSelectionModel().getSelectedItem()!= null){
 int codigoResponsableTurno = ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno();
    Map parametros = new HashMap();
    parametros.put("p_codigoResponsableTurno", codigoResponsableTurno);
    GenerarReporte.mostrarReporte("ResponsableTurnoReport.jasper", "Reporte Reponsable Turno", parametros);
    
 }else{
                    JOptionPane.showMessageDialog(null," Debe Seleccionar un Elemento");
                }
 
 }
}

    


