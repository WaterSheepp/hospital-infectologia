package org.alvaroramirez.controller;



import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.alvaroramirez.sistema.Principal;

public class MenuPrincipalController implements Initializable {
    private   Principal escenarioPrincipal;
    
@Override
   public void initialize(URL location, ResourceBundle resources) {
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
 public void ventanaMedicos(){
 escenarioPrincipal.ventanaMedicos();
 }
 public void ventanaPacientes(){
 escenarioPrincipal.ventanaPacientes();
 }
 public void ventanaProgramador(){
 escenarioPrincipal.ventanaProgramador();
 }
 public void ventanaTelefonoMedico(){
 escenarioPrincipal.ventanaTelefonoMedico();
 }
 public void ventanaAreas(){
 escenarioPrincipal.ventanaAreas();
 }
 public void ventanaContactoUrgencias(){
 escenarioPrincipal.ventanaContactoUrgencia();
 }
  public void ventanaEspecialidad(){
 escenarioPrincipal.ventanaEspecialidad();
 }
    public void ventanaMedicoEspecialidad(){
  escenarioPrincipal.ventanaMedicoEspecialidad();
  }
        public void ventanaCargo(){
  escenarioPrincipal.ventanaCargo();
  }
   public void ventanaTurno(){
 escenarioPrincipal.ventanaTurno();
 }
   public void ventanaResponsableTurno(){
   
   escenarioPrincipal.ventanaResponsableTurno();
   }
   public void ventanaHorarios (){
   escenarioPrincipal.ventanaHorarios();
   }
    public void ventanaReporteCita (){
   escenarioPrincipal.ventanaReporteCita();
   }
    public void CloseApp(ActionEvent event){
        Platform.exit();        
        System.exit(0);
}
}
