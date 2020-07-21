

package org.alvaroramirez.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.alvaroramirez.report.GenerarReporte;
import org.alvaroramirez.sistema.Principal;



    
    public class ReporteCitaController implements Initializable {
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
   public void menuPrincipal (){
    escenarioPrincipal.menuPrincipal();
}
   
          public void MedicoGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("MedicoGeneral.jasper", "Reporte Medico ", parametros);
    

 }
           public void TelefonoMedicoGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("TelefonoMedicoGeneral.jasper", "Reporte  Telefono Medico ", parametros);
    

 }
            public void HorariosGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("HorariosGeneral.jasper", "Reporte  HorariosGeneral ", parametros);
    

 }
            public void EspecialidadesGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("EspecialidadesGeneral.jasper", "Reporte  EspecialidadesGeneral ", parametros);
    

 }
                public void MedicoEspecialidaReporte(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("MedicoEspecialidaReporte.jasper", "Reporte  MedicoEspecialidaReporte ", parametros);
    

 }
                public void PacienteGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("PacienteGeneral.jasper", "Reporte  PacienteGeneral ", parametros);
    

 }
                
                public void ContactoUrgenciaGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("ContactoUrgenciaGeneral.jasper", "Reporte  ContactoUrgenciaGeneral ", parametros);
    

 }
                 public void AreasGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("AreasGeneral.jasper", "Reporte  AreasGeneral ", parametros);
    

 }
                  public void CargosGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("CargosGeneral.jasper", "Reporte  CargosGeneral ", parametros);
    

 }
                    public void ResponsableTurnoGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("ResponsableTurnoGeneral.jasper", "Reporte  ResponsableTurnoGeneral ", parametros);
    

 }
                     public void TurnoGeneral(){
    Map parametros = new HashMap();
    GenerarReporte.mostrarReporte("TurnoGeneral.jasper", "Reporte  TurnoGeneral ", parametros);
    

 }
    }