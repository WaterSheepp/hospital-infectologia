create database DBHospitalInfectologia2018025;
use DBHospitalInfectologia2018025;

alter user 'root'@'localhost' identified with mysql_native_password by 'admin';
-- ---------------------------------------------------------
--  TABLA AREAS
-- ---------------------------------------------------------

CREATE TABLE Areas (
    codigoArea INT NOT NULL AUTO_INCREMENT,
    nombreArea VARCHAR(45),
    PRIMARY KEY (codigoArea)
);
-- ---------------------------------------------------------
--  TABLA  CARGOS
-- ---------------------------------------------------------
 
 CREATE TABLE Cargos (
    codigoCargo INT NOT NULL AUTO_INCREMENT,
    nombreCargo VARCHAR(45),
    PRIMARY KEY (codigoCargo)
);

-- ---------------------------------------------------------
--  TABLA PACIENTES
-- ---------------------------------------------------------
CREATE TABLE Pacientes (
    codigoPaciente INT NOT NULL AUTO_INCREMENT,
    DPI VARCHAR(20) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    fechaNacimiento DATE,
    edad INT,
    direccion VARCHAR(150) NOT NULL,
    ocupacion VARCHAR(50) NOT NULL,
    sexo VARCHAR(15) NOT NULL,
    PRIMARY KEY (codigoPaciente)
);

-- ---------------------------------------------------------
--  TABLA CONTACTO URGENCIA
-- ---------------------------------------------------------

CREATE TABLE contactoUrgencia (
    codigoContactoUrgencia INT NOT NULL AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    numeroContacto VARCHAR(10) NOT NULL,
    pacientes_codigoPaciente INT NOT NULL,
    PRIMARY KEY (codigoContactoUrgencia),
    FOREIGN KEY (pacientes_codigoPaciente)
        REFERENCES pacientes (codigoPaciente)
);

-- ---------------------------------------------------------
--  TABLA ESPECIALIDADES
-- ---------------------------------------------------------
CREATE TABLE Especialidades (
    codigoEspecialidad INT NOT NULL AUTO_INCREMENT,
    nombreEspecialidad VARCHAR(45),
    PRIMARY KEY (codigoEspecialidad)
);
-- ---------------------------------------------------------
--  TABLA HORARIOS
-- ---------------------------------------------------------
CREATE TABLE Horarios (
    codigoHorario INT NOT NULL AUTO_INCREMENT,
    horarioInicio DATETIME NOT NULL,
    horarioSalida DATETIME NOT NULL,
    lunes TINYINT,
    martes TINYINT,
    miercoles TINYINT,
    jueves TINYINT,
    viernes TINYINT,
    PRIMARY KEY (codigoHorario)
);
-- ---------------------------------------------------------
--  TABLA MEDICOS
-- ---------------------------------------------------------

CREATE TABLE Medicos (
    codigoMedico INT AUTO_INCREMENT NOT NULL,
    licenciaMedica INT NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    horaEntrada datetime NOT NULL,
    horaSalida datetime NOT NULL,
    sexo VARCHAR(15) NOT NULL,
    PRIMARY KEY (codigoMedico)
); 
-- ---------------------------------------------------------
--  TABLA  MEDICO ESPECILIDAD
-- ---------------------------------------------------------
CREATE TABLE Medico_Especialidad (
    codigoMedicoEspecialidad INT NOT NULL AUTO_INCREMENT,
    codigoMedico INT NOT NULL,
    codigoEspecialidad INT NOT NULL,
    codigoHorario INT NOT NULL,

    PRIMARY KEY (codigoMedicoEspecialidad),
    
    FOREIGN KEY (codigoEspecialidad)
        REFERENCES Especialidades (codigoEspecialidad),
        
    FOREIGN KEY (codigoHorario)
        REFERENCES Horarios (codigoHorario),
        
    FOREIGN KEY (codigoMedico)
        REFERENCES Medicos (codigoMedico)
);



-- ---------------------------------------------------------
--  TABLA  RESPONSABLE TURNO
-- ---------------------------------------------------------
CREATE TABLE ResponsableTurno (
    codigoResponsableTurno INT NOT NULL AUTO_INCREMENT,
    nombreResponsable VARCHAR(75) NOT NULL,
    apellidosResponsable VARCHAR(75) NOT NULL,
    telefonoPersonal VARCHAR(10) NOT NULL,
    codigoArea INT NOT NULL,
    codigoCargo INT NOT NULL,
   
    PRIMARY KEY (codigoResponsableTurno),
    FOREIGN KEY (codigoArea)
        REFERENCES areas (codigoArea),
    FOREIGN KEY (codigoCargo)
        REFERENCES cargos (codigoCargo)
);
-- ---------------------------------------------------------
--  TABLA TELEFONOS MEDICO
-- ---------------------------------------------------------

CREATE TABLE telefonosMedico (
    codigoTelefonoMedico INT NOT NULL AUTO_INCREMENT,
    telefonoPersonal VARCHAR(15) NOT NULL,
    telefonoTrabajo VARCHAR(15) NOT NULL,
    codigoMedico INT NOT NULL,
    PRIMARY KEY (codigoTelefonoMedico),
    FOREIGN KEY (codigoMedico)
        REFERENCES medicos (codigoMedico)
);
-- ---------------------------------------------------------
--  TABLA TURNO
-- ---------------------------------------------------------
    
CREATE TABLE Turno (
    codigoTurno INT NOT NULL AUTO_INCREMENT,
    fechaTurno DATE NOT NULL,
    fechaCita DATE NOT NULL,
    valorCita DECIMAL(10 , 2 ) NOT NULL,
    codigoMedicoEspecialidad INT NOT NULL,
    codigoResponsableTurno INT NOT NULL,
    codigoPaciente INT NOT NULL,
   
    PRIMARY KEY (codigoTurno),
    FOREIGN KEY (codigoPaciente)
        REFERENCES pacientes (codigoPaciente),
    FOREIGN KEY (codigoMedicoEspecialidad)
        REFERENCES medico_especialidad (codigoMedicoEspecialidad),
    FOREIGN KEY (codigoResponsableTurno)
        REFERENCES responsableturno (codigoResponsableTurno)
);
-- ________________________________________________________________
-- 					P R O C E D I M I E N T O S
-- 				modificar,agregar,eliminar,buscar,listar
-- ________________________________________________________________

-- ---------------------------------------------------------
--  PROCEDIMIENTOS  MEDICOS 
-- ---------------------------------------------------------


Delimiter $$
create procedure sp_AgregarMedicos ( p_licenciaMedica int , p_nombres varchar(100),p_apellidos varchar(100),p_horaEntrada datetime ,p_horaSalida datetime  ,p_sexo varchar (15))
begin 
insert into Medicos(licenciaMedica,nombres,apellidos,horaEntrada,horaSalida,sexo)

		 values (p_licenciaMedica, p_nombres,p_apellidos ,p_horaEntrada,p_horaSalida ,p_sexo );
		end$$	
 
delimiter;
select * from Medicos;

call sp_AgregarMedicos (21456,'Antonio Miguel','Sanchez Paz','07:00:00','17:00:00','Masculino');
call sp_AgregarMedicos (45879,'Mike Stuart ','Thompson Lee','17:00:00','07:00:00','Masulino');
call sp_AgregarMedicos (44122,'Jennifer Abigail','Castillo Herrarte','10:00:00','15:00:00','Femenino');
call sp_AgregarMedicos (84789,'Angel Omar','Perez Herrarte','18:00:00','02:00:00','Masculino');
call sp_AgregarMedicos (98754,'Sebastian Alejandro','Castro Hernandes','11:00:00','16:00:00','Masculino');

-- -----------------------
Delimiter $$
create procedure sp_Buscar_Medico(p_codigoMedico int)

begin
select *  from Medicos where codigoMedico = p_codigoMedico;
end $$
select *  from Medicos;
call sp_Buscar_Medico

DELIMITER $$ 
create procedure sp_ModificarMedicos (p_codigoMedico int, p_licenciaMedica int , p_nombres varchar(100),p_apellidos varchar(100),p_horaEntrada datetime ,p_horaSalida datetime ,p_sexo varchar (15))
	begin 
    
		update Medicos 
        set licenciaMedica = p_licenciaMedica,
        nombres = p_nombres,
        apellidos = p_apellidos,
        horaEntrada = p_horaEntrada, 
        horaSalida = p_horaSalida ,
        sexo = p_sexo
        where codigoMedico = p_codigoMedico;
        
        end $$
        
        call sp_ModificarMedicos ()

DELIMITER $$
	create procedure sp_EliminarMedicos(p_codigoMedico int)
     begin
	delete from Medicos  where codigoMedico = p_codigoMedico;
   END$$

DELIMITER $$ 
create procedure sp_MostrarMedicos( )
begin
	select codigoMedico,licenciaMedica,nombres,apellidos,horaEntrada,horaSalida,sexo from Medicos;
    end $$
call sp_MostrarMedicos
-- ---------------------------------------------------------
--  PROCEDIMIENTOS  TELEFONOS MEDICOS
-- ---------------------------------------------------------



Delimiter $$
create procedure sp_AgregarTelefonosMedico(p_telefonoPersonal varchar(15), p_telefonoTrabajo varchar(15),p_codigoMedico int)
begin 
insert into telefonosMedico(telefonoPersonal,telefonoTrabajo,codigoMedico)

		 values (p_telefonoPersonal,p_telefonoTrabajo,p_codigoMedico);
		end$$	
 
delimiter;

call sp_AgregarTelefonosMedico ('54552288','31554624',1);
call sp_AgregarTelefonosMedico ('43098206','42993385',2);
call sp_AgregarTelefonosMedico ('66021541','66001244',3);
call sp_AgregarTelefonosMedico ('55654482','32549888',4);
call sp_AgregarTelefonosMedico ('32487651','51248365',5);



-- -----------------------
Delimiter $$
create procedure sp_Buscar_telefonosMedico(p_codigoTelefonoMedico int)

begin
select *  from telefonosMedico where codigoTelefonoMedico = p_codigoTelefonoMedico;
end $$
call sp_Buscar_telefonosMedico (1)



DELIMITER $$ 
create procedure sp_ModificartelefonosMedico(p_codigoTelefonoMedico int,p_telefonoPersonal varchar(15), p_telefonoTrabajo varchar(15),p_codigoMedico int)

	begin 
    
		update telefonosMedico 
        set codigoTelefonoMedico = p_codigotelefonoMedico,   
        telefonoPersonal = p_telefonoPersonal,
        codigoMedico=p_codigoMedico
        where codigoTelefonoMedico = p_codigoTelefonoMedico;
        
        end $$

DELIMITER $$
	create procedure sp_EliminarTelefonosMedico(p_codigoTelefonoMedico int)
     begin
	delete from telefonosMedico  where codigoTelefonoMedico = p_codigoTelefonoMedico;
   END$$

DELIMITER $$ 
create procedure sp_MostrartelefonosMedico( )
begin
	select codigoTelefonoMedico,telefonoPersonal,telefonoTrabajo,codigoMedico from telefonosMedico;
    
end $$
call sp_MostrartelefonosMedico
-- ---------------------------------------------------------
--  PROCEDIMIENTOS  HORARIOS 
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_AgregarHorarios ( p_horarioInicio datetime ,p_horarioSalida datetime,p_lunes tinyint ,p_martes tinyint,p_miercoles tinyint,p_jueves tinyint, p_viernes tinyint)
begin 
insert into  Horarios (horarioInicio,horarioSalida,lunes,martes,miercoles,jueves,viernes)

		 values ( p_horarioInicio,p_horarioSalida,p_lunes,p_martes,p_miercoles,p_jueves,p_viernes);
		end$$	
 
delimiter;

call sp_AgregarHorarios ('2019-02-01 ','2019-02-8' ,1,1,1,1,0);
call sp_AgregarHorarios ('2018-10-11 ','2019-01-1' ,1,1,1,1,1);
call sp_AgregarHorarios ('2019-12-10 ','2019-12-11' ,1,0,0,0,0);
call sp_AgregarHorarios ('2019-05-21 ','2019-05-28' ,1,1,0,1,1);
call sp_AgregarHorarios ('2019-04-25 ','2019-04-29', 1,1,0,0,0);


-- ---------------------------------------------------------
Delimiter $$
create procedure sp_Buscar_Horarios(p_codigoHorario int)

begin
select *  from Horarios where codigoHorario = p_codigoHorario;
end $$

delimiter;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_ModificarHorarios (p_codigoHorario int,p_horarioInicio datetime ,p_horarioSalida datetime,p_lunes tinyint ,p_martes tinyint,p_miercoles tinyint,p_jueves tinyint, p_viernes tinyint)
	begin 
    
		update Horarios
        set horarioInicio = p_horarioInicio,
        horarioSalida=p_horarioSalida,
        lunes=p_lunes,
        martes=p_martes,
        miercoles=p_miercoles,
        jueves =p_jueves,
        viernes=p_viernes
        where codigoHorario = p_codigoHorario;
        
        end $$
delimiter;
-- ---------------------------------------------------------
DELIMITER $$
	create procedure sp_EliminarHorarios(p_codigoHorario int)
     begin
	delete from Horarios  where codigoHorario  = p_codigoHorario;
   END$$
delimiter ;

-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_MostrarHorarios( )
begin
	select codigoHorario,horarioInicio,horarioSalida,lunes,martes,miercoles,jueves,viernes from Horarios ;
    end $$
    call sp_MostrarHorarios
 -- ---------------------------------------------------------
--  PROCEDIMIENTOS  ESPECIALIDADES
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_AgregarEspecialidades ( p_nombreEspecialidad varchar (45))
begin 
insert into Especialidades(nombreEspecialidad)

		 values (p_nombreEspecialidad);
		end$$	
 
delimiter;

call sp_AgregarEspecialidades ('Neurologia ');
call sp_AgregarEspecialidades ('Cardiologia');
call sp_AgregarEspecialidades ('Otorrinolaringologia');
call sp_AgregarEspecialidades ('Psicologia');
call sp_AgregarEspecialidades ('Urologia');

-- ---------------------------------------------------------
Delimiter $$
create procedure sp_Buscar_Especialidades(p_codigoEspecialidad int)

begin
select *  from Especialidades where codigoEspecialidad = p_codigoEspecialidad;
end $$

delimiter;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_ModificarEspecialidades (p_codigoEspecialidad int, p_nombreEspecialidad varchar(45))
	begin 
    
		update Especialidades
        set nombreEspecialidad = p_nombreEspecialidad
        where codigoEspecialidad = p_codigoEspecialidad;
        
        end $$


-- ---------------------------------------------------------
DELIMITER $$
	create procedure sp_EliminarEspecialidades(p_codigoEspecialidad int)
     begin
	delete from Especialidades  where codigoEspecialidad  = p_codigoEspecialidad;
   END$$

-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_MostrarEspecialidades( )
begin
	select codigoEspecialidad,nombreEspecialidad from Especialidades ;
    end $$   
    
	-- ---------------------------------------------------------
--  PROCEDIMIENTOS  MEDICO ESPECIALIDAD
-- ---------------------------------------------------------

Delimiter $$
create procedure sp_AgregarMedicoEspecialidades( p_codigoMedico int ,p_codigoEspecialidad int ,p_codigoHorario int)
begin 
insert into Medico_Especialidad (codigoMedico,codigoEspecialidad,codigoHorario)

		 values (p_codigoMedico,p_codigoEspecialidad,p_codigoHorario);
		end$$	

--     drop procedure sp_AgregarMedicoEspecialidades;
 select * from cargos




call sp_AgregarMedicoEspecialidades (1,1,1);
call sp_AgregarMedicoEspecialidades (2,2,2);
call sp_AgregarMedicoEspecialidades (3,3,3);
call sp_AgregarMedicoEspecialidades (4,4,4);
call sp_AgregarMedicoEspecialidades (5,5,5);


-- -----------------------
Delimiter $$
create procedure sp_Buscar_MedicoEspecialidades(p_codigoMedicoEspecialidad int)

begin
select *  from Medico_Especialidad where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
end $$



DELIMITER $$ 
create procedure sp_ModificarMedicoEspecialidades (p_codigoMedicoEspecialidad int,p_codigoMedico int , p_codigoEspecialidad int ,p_codigoHorario int)
	begin 
		update Medico_Especialidad 
        set codigoMedico = p_codigoMedico,
        codigoEspecialidad = p_codigoEspecialidad,        
        codigoHorario = p_codigoHorario
        where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
        
        end $$
        

DELIMITER $$
	create procedure sp_EliminarMedicoEspecialidades(p_codigoMedicoEspecialidad int)
     begin
	delete from Medico_Especialidad  where codigoMedicoEspecialidad = p_codigoMedicoEspecialidad;
   END$$
call  sp_EliminarMedicoEspecialidades (2)

DELIMITER $$ 
create procedure sp_MostrarMedicosEspecialidades( )
begin
	select codigoMedicoEspecialidad,codigoMedico,codigoEspecialidad,codigoHorario from Medico_Especialidad;
    end $$
    

    -- ---------------------------------------------------------
--  PROCEDIMIENTOS  PACIENTES
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_AgregarPacientes ( p_DPI varchar (20),p_apellidos varchar(100),p_nombres varchar(100),p_fechaNacimiento date,
p_edad int,p_direccion varchar(150),p_ocupacion varchar (50),p_sexo varchar (15))
begin 
insert into Pacientes(DPI,apellidos,nombres,fechaNacimiento,edad,direccion,ocupacion,sexo)

		 values ( p_DPI,p_apellidos,p_nombres,p_fechaNacimiento,p_edad,p_direccion,p_ocupacion,p_sexo);
 
end $$
call sp_AgregarPacientes ('4568 32165 7894','Castilo Martinez','Olga Marisol','1990-05-02'
	,29,'15av 9-87 Residencial las Flores','Secretaria','Femenino');
call sp_AgregarPacientes ('7899 14556 6459','Cadena Palacios','Erick Fernando','1992-07-12'
	,27,'7av 3-78 zona 8','Ingeniero','Masculino');
call sp_AgregarPacientes ('8546 12345 1312','Garcia Chaves','Claudia Valeria','1982-11-10'
	,37,'12calle 14-88 Residencial las llamas','Empresaria','Femenino');
call sp_AgregarPacientes ('1658 89445 4512','Melendes Juarez','Cristopher Samuel','1982-11-10'
	,37,'10av 19-84 zona 1 ','policia','Maculino');
call sp_AgregarPacientes ('8456 94864 2456','Ramirez Alvarez','Alvaro Gabriel','1982-11-10'
	,37,'7calle 8-55 Residencial las verapaces','Estudiabte','Masculino');


-- ---------------------------------------------------------
Delimiter $$
create procedure sp_Buscar_Pacientes(p_codigoPaciente int)

begin
select *  from Pacientes where codigoPaciente = p_codigoPaciente;
end $$

delimiter;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_ModificarPacientes (p_codigoPaciente int,p_DPI varchar (20),p_apellidos varchar(100),p_nombres varchar(100),
p_fechaNacimiento date,p_edad int,p_direccion varchar(150),p_ocupacion varchar (50),p_sexo varchar (15))
	begin 
    
		update Pacientes
        set DPI = p_DPI,
        apellidos = p_apellidos,
        nombres = p_nombres,
        fechaNacimiento = p_fechaNacimiento,
        edad = p_edad,
        direccion = p_direccion,
        ocupacion = p_ocupacion ,
        sexo = p_sexo
        
        where codigoPaciente = p_codigoPaciente;
        
        end $$
delimiter;
-- ---------------------------------------------------------
DELIMITER $$
	create procedure sp_EliminarPacientes(p_codigoPaciente int)
     begin
	delete from Pacientes  where codigoPaciente  = p_codigoPaciente;
   END$$
delimiter ;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_MostrarPacientes( )
begin
	select codigoPaciente,DPI,apellidos,nombres,fechaNacimiento,edad,direccion,ocupacion,sexo from Pacientes ;
    end $$
    
    call sp_MostrarPacientes( )
-- ---------------------------------------------------------
--  PROCEDIMIENTOS  CONTACTO URGENCIA
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_AgregarContactoUrgencias( p_nombres varchar (100),p_apellidos varchar (100),p_numeroContacto varchar (10),p_pacientes_codigoPaciente int)
begin 
insert into ContactoUrgencia(nombres,apellidos,numeroContacto,pacientes_codigoPaciente)

		 values (p_nombres,p_apellidos,p_numeroContacto,p_pacientes_codigoPaciente);
		end$$	

delimiter;

call sp_AgregarContactoUrgencias ('Tómas Jose','Alarcon Lemus','50131220',1);
call sp_AgregarContactoUrgencias (' Maria Rosalia','Arbea Perez','65483321',2);
call sp_AgregarContactoUrgencias ('Alejandro Josue','Bautista Contreras','46132149',3);
call sp_AgregarContactoUrgencias ('Juana Patricia','Castro Vera','51358020',4);
call sp_AgregarContactoUrgencias ('Franciso Juan','De Lira Velasco','31023208',5);

-- ---------------------------------------------------------
Delimiter $$
create procedure sp_Buscar_ContactoUrgencias(p_codigoContactoUrgencia int)

begin
select *  from ContactoUrgencia where codigoContactoUrgencia  = p_codigoContactoUrgencia;
end $$

delimiter;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_ModificarContactoUrgencias (p_codigoContactoUrgencia int,p_nombres varchar (100),p_apellidos varchar (100),p_numeroContacto varchar (10),p_pacientes_codigoPaciente int)
begin
    
		update ContactoUrgencia
        set nombres = p_nombres,
        apellidos = p_apellidos,
        numeroContacto = p_numeroContacto,
        pacientes_codigoPaciente =p_pacientes_codigoPaciente
        where codigoContactoUrgencia= p_codigoContactoUrgencia;
        
        end $$
delimiter;


-- ---------------------------------------------------------
DELIMITER $$
	create procedure sp_EliminarContactoUrgencias(p_codigoContactoUrgencia int)
     begin
	delete from ContactoUrgencia  where codigoContactoUrgencia  = p_codigoContactoUrgencia;
   END$$
delimiter ;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_MostrarContactoUrgencia( )
begin
	select codigoContactoUrgencia,nombres,apellidos,numeroContacto,pacientes_codigoPaciente from ContactoUrgencia ;
    end $$
    
    call sp_MostrarContactoUrgencia
    select * from contactourgencia

    
-- ---------------------------------------------------------
--  PROCEDIMIENTOS   AREAS
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_AgregarAreas ( p_nombreArea varchar (45))
begin 
insert into Areas(nombreArea)

		 values (p_nombreArea );
		end$$	
 
delimiter;

call sp_AgregarAreas ('Urgencias');
call sp_AgregarAreas ('Patologia');
call sp_AgregarAreas ('Banco de Sagre');
call sp_AgregarAreas ('Endoscopia');
call sp_AgregarAreas ('Cardiologia');
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_Buscar_Areas(p_codigoArea int)

begin
select *  from Areas where codigoArea = p_codigoArea;
end $$
select *  from Areas;
delimiter;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_ModificarAreas (p_codigoArea int, p_nombreArea varchar(45))
	begin 
    
		update Areas
        set nombreArea = p_nombreArea
        where codigoArea = p_codigoArea;
        
        end $$
delimiter;
-- ---------------------------------------------------------
DELIMITER $$
	create procedure sp_EliminarAreas(p_codigoArea int)
     begin
	delete from Areas  where codigoArea = p_codigoArea;
   END$$
delimiter ;
DELIMITER $$ 
create procedure sp_MostrarAreas( )
begin
	select codigoArea,nombreArea from Areas;
    end $$




-- ---------------------------------------------------------
--  PROCEDIMIENTOS   CARGOS
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_AgregarCargos ( p_nombreCargo varchar (45))
begin 
insert into Cargos(nombreCargo)

		 values (p_nombreCargo);
		end$$	
 
delimiter;

call sp_AgregarCargos ('Enfermeras');
call sp_AgregarCargos ('Tecnicos');
call sp_AgregarCargos ('Medicos ');
call sp_AgregarCargos ('Licenciados');
call sp_AgregarCargos ('Bioquimicos');
-- ---------------------------------------------------------
Delimiter $$
create procedure sp_Buscar_Cargos(p_codigoCargo int)

begin
select *  from Cargos where codigoCargo = p_codigoCargo;
end $$
select *  from Cargos;
delimiter;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_ModificarCargos (p_codigoCargo int, p_nombreCargo varchar(45))
	begin 
    
		update Cargos
        set nombreCargo = p_nombreCargo 
        where codigoCargo = p_codigoCargo;
        
        end $$
delimiter;


-- ---------------------------------------------------------
DELIMITER $$
	create procedure sp_EliminarCargos(p_codigoCargo int)
     begin
	delete from Cargos  where codigoCargo  = p_codigoCargo;
   END$$
delimiter ;
-- ---------------------------------------------------------
DELIMITER $$ 
create procedure sp_MostrarCargos( )
begin
	select codigoCargo,nombreCargo from Cargos ;
    end $$




-- ---------------------------------------------------------
--  PROCEDIMIENTOS  RESPONSABLE TURNO
-- ---------------------------------------------------------


Delimiter $$
create procedure sp_AgregarResponsableTurno(p_nombreResponsable varchar(75), p_apellidosResponsable varchar(75),p_telefonoPersonal varchar(10),p_codigoArea int,p_codigoCargo int)
begin 
insert into ResponsableTurno(nombreResponsable,apellidosResponsable,telefonoPersonal,codigoArea,codigoCargo)

		 values (p_nombreResponsable,p_apellidosResponsable,p_telefonoPersonal,p_codigoArea,p_codigoCargo);
		end$$	
 
delimiter;

call sp_AgregarResponsableTurno ('Miguel Angel','Bolaños Sanchez','54462201',1,2);
call sp_AgregarResponsableTurno ('Ernesto Alberto ','Arias Rodrigues','35547885',3,4);
call sp_AgregarResponsableTurno ('Oscar Guillermo','Sanchez Gallardo','30112230',2,3);
call sp_AgregarResponsableTurno ('Raul Genaro','Alejo Alcantar','56996144',4,5);
call sp_AgregarResponsableTurno ('Alfredo Javier','Perez Perez','43221549',5,1);







-- -----------------------
Delimiter $$
create procedure sp_Buscar_ResponsableTurno(p_codigoResponsableTurno int)

begin
select *  from ResponsableTurno where codigoResponsableTurno = p_codigoResponsableTurno;
end $$



DELIMITER $$ 
create procedure sp_ModificarResponsableTurno(p_codigoResponsableTurno int,p_nombreResponsable varchar(75),
 p_apellidosResponsable varchar(75),p_telefonoPersonal varchar(10),p_codigoArea int,p_codigoCargo int)

	begin 
    
		update ResponsableTurno 
        set nombreResponsable = p_nombreResponsable,
        apellidosResponsable = p_apellidosResponsable,        
        telefonoPersonal = p_telefonoPersonal,
        codigoArea =p_codigoArea,
        codigoCargo=p_codigoCargo
        where codigoResponsableTurno = p_codigoResponsableTurno;
        
        end $$

DELIMITER $$
	create procedure sp_EliminarResponsableTurno(p_codigoResponsableTurno int)
     begin
	delete from ResponsableTurno  where codigoResponsableTurno = p_codigoResponsableTurno;
   END$$

DELIMITER $$ 
create procedure sp_MostrarResponsableTurno( )
begin
	select codigoResponsableTurno,nombreResponsable,apellidosResponsable,telefonoPersonal,codigoArea,codigoCargo from ResponsableTurno;
    end $$
    

-- ---------------------------------------------------------
--  PROCEDIMIENTOS  TURNO
-- ---------------------------------------------------------



Delimiter $$
create procedure sp_AgregarTurno(p_fechaTurno date, p_fechaCita date,p_valorCita decimal (10,2),p_codigoMedicoEspecialidad int,p_codigoResponsableTurno int, p_codigoPaciente int)
begin 
insert into Turno (fechaTurno,fechaCita,valorCita,codigoMedicoEspecialidad,codigoResponsableTurno,codigoPaciente)

		 values (p_fechaTurno,p_fechaCita,p_valorCita,p_codigoMedicoEspecialidad,p_codigoResponsableTurno,p_codigoPaciente);
		end$$	
        call sp_AgregarTurno ('2018-05-03','2015-02-01',50.20,5,5,5)
 
delimiter;




-- -----------------------
Delimiter $$
create procedure sp_Buscar_Turno(p_codigoTurno int)

begin
select *  from Turno where codigoTurno = p_codigoTurno;
end $$



DELIMITER $$ 
create procedure sp_ModificarTurno(p_codigoTurno int,p_fechaTurno date, p_fechaCita date,p_valorCita decimal (10,2),p_codigoMedicoEspecialidad int,p_codigoResponsableTurno int, p_codigoPaciente int)

	begin 
    
		update Turno 
        set fechaTurno = p_fechaTurno,   
        fechaCita = p_fechaCita,
        valorCita=p_valorCita,
        codigoMedicoEspecialidad = p_codigoMedicoEspecialidad,
        codigoResponsableTurno = p_codigoResponsableTurno,
        codigoPaciente = p_codigoPaciente
        
        
        where codigoTurno = p_codigoTurno;
        
        end $$

DELIMITER $$
	create procedure sp_EliminarTurno(p_codigoTurno int)
     begin
	delete from Turno  where codigoTurno = p_codigoTurno;
   END$$

DELIMITER $$ 
create procedure sp_MostrarTurno( )
begin
	select codigoTurno,fechaTurno,fechaCita,valorCita,codigoMedicoEspecialidad,codigoResponsableTurno,codigoPaciente from Turno;
    
end $$


	