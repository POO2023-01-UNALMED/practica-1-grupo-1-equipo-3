import tkinter as tk
from tkinter import Menu
from tkinter import PhotoImage
import math
from tkinter import Toplevel
from tkinter import messagebox, Frame, Button, Label
from tkinter.ttk import Notebook
import math

import os
import sys
fpath = os.path.join(os.path.dirname(__file__), 'versionEnPython')
fpath = fpath.split(sep="\Banco")[0]
fpath = fpath.replace("\\", "/")
sys.path.append(fpath)

# Modulos propios de la apllicación
from Banco.src.gestorAplicacion.infraestructura.Banco import Banco
from Banco.src.gestorAplicacion.infraestructura.Canal import Canal
from Banco.src.gestorAplicacion.Entidades_de_negocio.Cliente import Cliente
from Banco.src.gestorAplicacion.Entidades_de_negocio.Divisa import Divisa
from Banco.src.gestorAplicacion.Entidades_de_negocio.Factura import Factura
from FieldFrame import FieldFrame
from Banco.src.gestorAplicacion.tarjetas.Tarjeta import Tarjeta
from Banco.src.gestorAplicacion.tarjetas.TarjetaCredito import TarjetaCredito
from Banco.src.gestorAplicacion.tarjetas.TarjetaDebito import TarjetaDebito
from Banco.src.gestorAplicacion.Entidades_de_negocio.Transaccion import Transaccion
from Banco.src.baseDatos.Serializador import Serializador
from Banco.src.baseDatos.Deserializador import Deserializador
from Banco.src.gestorAplicacion.Errores.ErrorInput import ErrorInput
from Banco.src.gestorAplicacion.Errores.ErrorOpciones import ErrorOpciones




clientes = []
canales = []
def setup():
    cliente1 = Cliente("Dario Gomez", 1)
    clientes.append(cliente1)
    cliente2 = Cliente("Esteban Betancur", 2)
    clientes.append(cliente2)
    cliente3 = Cliente("Marta Martínez", 3)
    clientes.append(cliente3)
    cliente4 = Cliente("Sandra Lopez", 4)
    clientes.append(cliente4)
    cliente5 = Cliente("Yasuri Yamile", 5)
    clientes.append(cliente5)

    tarjetaDebito1 = TarjetaDebito(123456, Divisa.DOLAR, 3000)
    tarjetaDebito2 = TarjetaDebito(234567, Divisa.EURO, 500)
    tarjetaDebito3 = TarjetaDebito(345678, Divisa.RUBLO_RUSO, 10000)
    tarjetaDebito4 = TarjetaDebito(456789, Divisa.YEN_JAPONES, 100000)
    tarjetaDebito5 = TarjetaDebito(567890, Divisa.PESO_COLOMBIANO, 5000000)
    tarjetaDebito6 = TarjetaDebito(678901, Divisa.DOLAR, 200)
    tarjetaDebito7 = TarjetaDebito(789012, Divisa.EURO, 1500)
    tarjetaDebito8 = TarjetaDebito(890123, Divisa.RUBLO_RUSO, 5000)
    tarjetaDebito9 = TarjetaDebito(901234, Divisa.YEN_JAPONES, 50000)
    tarjetaDebito10 = TarjetaDebito(101112, Divisa.PESO_COLOMBIANO, 10000000)
    tarjetaDebito11 = TarjetaDebito(111213, Divisa.LIBRA_ESTERLINA, 100)
    tarjetaDebito12 = TarjetaDebito(121314, Divisa.DOLAR, 1000)
    tarjetaDebito13 = TarjetaDebito(131415, Divisa.EURO, 2000)
    tarjetaDebito14 = TarjetaDebito(141516, Divisa.RUBLO_RUSO, 7000)
    tarjetaDebito15 = TarjetaDebito(151617, Divisa.YEN_JAPONES, 80000)
    tarjetaDebito16 = TarjetaDebito(161718, Divisa.PESO_COLOMBIANO, 2000000)
    tarjetaDebito17 = TarjetaDebito(171819, Divisa.LIBRA_ESTERLINA, 50)
    tarjetaDebito18 = TarjetaDebito(181920, Divisa.DOLAR, 400)
    tarjetaDebito19 = TarjetaDebito(192021, Divisa.EURO, 2500)
    tarjetaDebito20 = TarjetaDebito(202122, Divisa.RUBLO_RUSO, 8000)
    tarjetaDebito21 = TarjetaDebito(212223, Divisa.YEN_JAPONES, 90000)
    tarjetaDebito22 = TarjetaDebito(222324, Divisa.PESO_COLOMBIANO, 3000000)
    tarjetaDebito23 = TarjetaDebito(232425, Divisa.LIBRA_ESTERLINA, 70)
    tarjetaDebito24 = TarjetaDebito(242526, Divisa.DOLAR, 800)
    tarjetaDebito25 = TarjetaDebito(252627, Divisa.EURO, 3000)
    tarjetaDebito26 = TarjetaDebito(262728, Divisa.RUBLO_RUSO, 6000)
    tarjetaDebito27 = TarjetaDebito(272829, Divisa.YEN_JAPONES, 70000)
    tarjetaDebito28 = TarjetaDebito(283930, Divisa.PESO_COLOMBIANO, 4000000)
    tarjetaDebito29 = TarjetaDebito(293031, Divisa.LIBRA_ESTERLINA, 90)
    tarjetaDebito30 = TarjetaDebito(303132, Divisa.DOLAR, 600)
    tarjetaDebito31 = TarjetaDebito(987654, Divisa.DOLAR, 5000)
    tarjetaDebito32 = TarjetaDebito(8765432, Divisa.EURO, 1000)
    tarjetaDebito33 = TarjetaDebito(7654321, Divisa.RUBLO_RUSO, 25000)
    tarjetaDebito34 = TarjetaDebito(6543210, Divisa.YEN_JAPONES, 100000)
    tarjetaDebito35 = TarjetaDebito(5432109, Divisa.PESO_COLOMBIANO, 5000000)
    tarjetaDebito36 = TarjetaDebito(4321098, Divisa.DOLAR, 300)
    tarjetaDebito37 = TarjetaDebito(3210987, Divisa.EURO, 1500000)
    tarjetaDebito38 = TarjetaDebito(2109876, Divisa.RUBLO_RUSO, 20000)
    tarjetaDebito39 = TarjetaDebito(1098765, Divisa.YEN_JAPONES, 75000)
    tarjetaDebito40 = TarjetaDebito(98765432, Divisa.PESO_COLOMBIANO, 3500000)
    tarjetaDebito41 = TarjetaDebito(246813, Divisa.DOLAR, 8000)
    tarjetaDebito42 = TarjetaDebito(1357924, Divisa.EURO, 500)
    tarjetaDebito43 = TarjetaDebito(9876543, Divisa.RUBLO_RUSO, 30000)
    tarjetaDebito44 = TarjetaDebito(8765432, Divisa.YEN_JAPONES, 50000)
    tarjetaDebito45 = TarjetaDebito(7654321, Divisa.PESO_COLOMBIANO, 10000000)
    tarjetaDebito46 = TarjetaDebito(6543210, Divisa.DOLAR, 200)
    tarjetaDebito47 = TarjetaDebito(5432109, Divisa.EURO, 100000)
    tarjetaDebito48 = TarjetaDebito(123456789, Divisa.LIBRA_ESTERLINA, 5000)
    tarjetaDebito49 = TarjetaDebito(345678901, Divisa.LIBRA_ESTERLINA, 10000)
    tarjetaDebito50 = TarjetaDebito(456789012, Divisa.LIBRA_ESTERLINA, 20000)

    tarjetaCredito1 = TarjetaCredito(987456, Divisa.DOLAR, 3000, 1.5)
    tarjetaCredito2 = TarjetaCredito(876543, Divisa.EURO, 2000, 2.0)
    tarjetaCredito3 = TarjetaCredito(765432, Divisa.RUBLO_RUSO, 105000, 0.8)
    tarjetaCredito4 = TarjetaCredito(654321, Divisa.YEN_JAPONES, 108050, 0.2)
    tarjetaCredito5 = TarjetaCredito(543210, Divisa.PESO_COLOMBIANO, 6000000, 1.25)
    tarjetaCredito6 = TarjetaCredito(432109, Divisa.DOLAR, 5000, 0.75)
    tarjetaCredito7 = TarjetaCredito(321098, Divisa.EURO, 1750, 0.1)
    tarjetaCredito8 = TarjetaCredito(210987, Divisa.RUBLO_RUSO, 70000, 0.3)
    tarjetaCredito9 = TarjetaCredito(109876, Divisa.YEN_JAPONES, 40000, 0.99)
    tarjetaCredito10 = TarjetaCredito(987654321, Divisa.PESO_COLOMBIANO, 1000000, 5.0)
    tarjetaCredito11 = TarjetaCredito(41341395, Divisa.LIBRA_ESTERLINA, 5000, 1.0)
    tarjetaCredito12 = TarjetaCredito(15641687, Divisa.LIBRA_ESTERLINA, 2000, 0.075)
    tarjetaCredito13 = TarjetaCredito(74857485, Divisa.DOLAR, 6000, 1.2)
    tarjetaCredito14 = TarjetaCredito(38475895, Divisa.EURO, 2500, 0.5)
    tarjetaCredito15 = TarjetaCredito(98347598, Divisa.RUBLO_RUSO, 80000, 0.6)
    tarjetaCredito16 = TarjetaCredito(57649827, Divisa.YEN_JAPONES, 70000, 0.3)
    tarjetaCredito17 = TarjetaCredito(123456789, Divisa.PESO_COLOMBIANO, 1500000, 2.5)
    tarjetaCredito18 = TarjetaCredito(43789563, Divisa.LIBRA_ESTERLINA, 3000, 0.8)
    tarjetaCredito19 = TarjetaCredito(79253485, Divisa.DOLAR, 4000, 1.0)
    tarjetaCredito20 = TarjetaCredito(21394785, Divisa.EURO, 1500, 0.3)
    tarjetaCredito21 = TarjetaCredito(19384756, Divisa.RUBLO_RUSO, 60000, 0.7)
    tarjetaCredito22 = TarjetaCredito(54879324, Divisa.YEN_JAPONES, 50000, 0.5)
    tarjetaCredito23 = TarjetaCredito(28573649, Divisa.PESO_COLOMBIANO, 2000000, 3.0)
    tarjetaCredito24 = TarjetaCredito(64329875, Divisa.LIBRA_ESTERLINA, 4000, 1.2)
    tarjetaCredito25 = TarjetaCredito(78345364, Divisa.DOLAR, 4500, 0.9)
    tarjetaCredito26 = TarjetaCredito(37825375, Divisa.EURO, 1800, 0.4)
    tarjetaCredito27 = TarjetaCredito(23758634, Divisa.RUBLO_RUSO, 50000, 0.6)
    tarjetaCredito28 = TarjetaCredito(23849570, Divisa.YEN_JAPONES, 60000, 0.4)
    tarjetaCredito29 = TarjetaCredito(95834575, Divisa.PESO_COLOMBIANO, 3000000, 4.0)
    tarjetaCredito30 = TarjetaCredito(58743634, Divisa.LIBRA_ESTERLINA, 5000, 1.5)

    cliente1.agregarTarjetasDebito(tarjetaDebito1, tarjetaDebito2, tarjetaDebito3, tarjetaDebito5, tarjetaDebito11, tarjetaDebito30, tarjetaDebito33, tarjetaDebito39)
    cliente1.agregarTarjetasCredito(tarjetaCredito1, tarjetaCredito2, tarjetaCredito7, tarjetaCredito9,tarjetaCredito11, tarjetaCredito17, tarjetaCredito22)

    cliente2.agregarTarjetasDebito(tarjetaDebito6, tarjetaDebito31, tarjetaDebito32, tarjetaDebito36, tarjetaDebito38, tarjetaDebito45, tarjetaDebito49)
    cliente2.agregarTarjetasCredito(tarjetaCredito3, tarjetaCredito5, tarjetaCredito10, tarjetaCredito20, tarjetaCredito28, tarjetaCredito26)

    cliente3.agregarTarjetasDebito(tarjetaDebito4, tarjetaDebito7, tarjetaDebito9, tarjetaDebito34, tarjetaDebito37, tarjetaDebito42, tarjetaDebito43)
    cliente3.agregarTarjetasCredito(tarjetaCredito6, tarjetaCredito8, tarjetaCredito12, tarjetaCredito18,  tarjetaCredito21)

    cliente4.agregarTarjetasDebito(tarjetaDebito8, tarjetaDebito10, tarjetaDebito40, tarjetaDebito35, tarjetaDebito41, tarjetaDebito47)
    cliente4.agregarTarjetasCredito(tarjetaCredito4, tarjetaCredito13, tarjetaCredito15, tarjetaCredito23, tarjetaCredito27)

    cliente5.agregarTarjetasDebito(tarjetaDebito12, tarjetaDebito44, tarjetaDebito46, tarjetaDebito48, tarjetaDebito50)
    cliente5.agregarTarjetasCredito(tarjetaCredito14, tarjetaCredito16, tarjetaCredito24, tarjetaCredito30, tarjetaCredito29)



    factura1 = Factura(cliente1, 1000000.0, 12, tarjetaDebito16)
    factura2 = Factura(cliente2, 9000000.0, 36, tarjetaDebito28)
    factura3 = Factura(cliente3, 56.0, 4, tarjetaDebito29)
    factura4 = Factura(cliente4, 16000, 8, tarjetaDebito20)
    factura5 = Factura(cliente5, 800000.0, 8, tarjetaDebito22)
    factura6 = Factura(cliente1, 24440, 6, tarjetaDebito27)
    factura7 = Factura(cliente2, 150.0, 2, tarjetaDebito24)
    factura10 = Factura(cliente5, 20000.0, 14, tarjetaDebito26)
    factura11 = Factura(cliente1, 200.0, 7, tarjetaDebito25)
    factura12 = Factura(cliente2, 150.0, 8, tarjetaDebito23)
    factura13 = Factura(cliente3, 180.0, 4, tarjetaDebito17)
    factura14 = Factura(cliente4, 120.0, 3, tarjetaDebito18)
    factura15 = Factura(cliente5, 90.0, 2, tarjetaDebito19)

    sucursalFisica1 = Canal("Sucursal Fisica", 2.0, 22500, 12370, 8000, 70000, 200000, 30000000)

    cajero1 = Canal("Cajero", 0.5)
    cajero1.setFondos(Divisa.PESO_COLOMBIANO, 12000000)
    canales.append(cajero1)

    corresponsal1 = Canal("Corresponsal Bancario", 1.0)
    corresponsal1.setFondos(Divisa.DOLAR, 20000)
    corresponsal1.setFondos(Divisa.EURO, 10000)
    corresponsal1.setFondos(Divisa.PESO_COLOMBIANO, 40000000)
    canales.append(corresponsal1)

    cajero2 = Canal("Cajero", 1.0)
    cajero2.setFondos(Divisa.DOLAR, 8000)
    canales.append(cajero2)

    sucursalFisica2 = Canal("Sucursal Física", 1.5, 4000.0, 6000.0, 2000.0, 150000.0, 8000000.0, 6700000.0)
    canales.append(sucursalFisica2)

    sucursalVirtual1 = Canal("Sucursal en Línea", 2.5, 5100.0, 8900.0, 0.0, 120000.0, 370000.0, 80545000.0)
    canales.append(sucursalFisica1)

    corresponsal2 = Canal("Corresponsal Bancario", 0.8)
    corresponsal2.setFondos(Divisa.DOLAR, 15000.0)
    corresponsal2.setFondos(Divisa.EURO, 8000.0)
    corresponsal2.setFondos(Divisa.RUBLO_RUSO, 350000.0)
    canales.append(corresponsal2)


#Serializacion Inicial (solo se ejecuta una vez)
# setup()
# Serializador.SerializarClientes(clientes)
# Serializador.SerializarCanales(canales)


#LO QUE YA FUNCIONA NO SE TOCA
#Inicializando variables de Banco a partir de la deserializacion
Banco.setClientes(Deserializador.DeserializarClientes())
Banco.setCanales(Deserializador.DeserializarCanales())
# Transaccion.setTransacciones(Deserializador.DeserializarCanales())

def serializacionAlCerrar():
    Serializador.SerializarClientes(Banco.getClientes())
    Serializador.SerializarCanales(Banco.getCanales())
    Serializador.SerializarTransacciones(Transaccion.getTransacciones())

    root.quit()

root = tk.Tk()

root.title("Banco Nacho")
root.iconbitmap("versionEnPython/assets/logo-unal.ico")  # Favicon de la apliación
root.protocol("WM_DELETE_WINDOW", serializacionAlCerrar)

# Creación de la ventana de inicio

# Menu de incio
def menuDescripcion():
    descripcion = Frame(p1)
    descripcion.pack(side='top', expand=True, fill='both')
    labelDes = Label(descripcion,
                     text="Aquí puedes conocer un poco más\nacerca de los desarrolladores de\nBanco Nacho, conocer algunas \nprevisualizaciones de nuestro sistema \ne ingresar a la aplicación de usuarios",
                     pady=8)
    labelDes.pack(expand=True, fill='both')
    labelDes.config(bg="#FFF2BC", fg="#856C00", font=("Arial", 12))

    buttonCerrarDes = Button(descripcion, text="Cerrar Descripción", command=lambda: descripcion.pack_forget(), padx=10,
                             pady=8)
    buttonCerrarDes.config(bg='#9A0000', fg='white', font=("Arial", 12, "bold"))
    buttonCerrarDes.pack(expand=True, fill="both")


navbar = Menu(root)
root.config(menu=navbar)

menu = Menu(navbar, tearoff=0)
navbar.add_cascade(label="Inicio", menu=menu)

menu.add_command(label="Salir", command=root.quit)
menu.add_command(label="Descripción", command=menuDescripcion)

# Ventana de la vista principal de inicio
ventaPrincipal = Frame(root)
ventaPrincipal.pack()


def abrirAplicacion():
    ventanaAplicacion.deiconify()
    root.withdraw()


p1 = Frame(ventaPrincipal, bg="green")
p1.pack(side="left", fill='y')

# Saludo de bienvenida
p3 = Frame(p1)
p3.pack(expand=True, fill='both')
labelP3 = Label(p3, text="Bienvenido a Banco Nacho\nTu banco de confianza\na unos simples clics de distancia", pady=8)
labelP3.pack(expand=True, fill='both')
labelP3.config(bg="#F5EFE7", fg="#73001B", font=("Arial", 12))

# Abrir la aplicacion del usuario
p4 = Frame(p1)
p4.pack(expand=True, fill='both')

# Fotos de p4
# Fotos del sistema
fotoSystem1 = PhotoImage(file="versionEnPython/assets/PantallazoApp1.png")
fotoSystem2 = PhotoImage(file="versionEnPython/assets/PantallazoApp2.png")
fotoSystem3 = PhotoImage(file="versionEnPython/assets/PantallazoApp3.png")
fotoSystem4 = PhotoImage(file="versionEnPython/assets/PantallazoApp4.png")
fotoSystem5 = PhotoImage(file="versionEnPython/assets/PantallazoApp5.png")

# Inicializamos el valor de image con la primera foto
labelFotoSystem = Label(p4, image=fotoSystem1)
labelFotoSystem.pack()

counterSystem = 1  # Nos ayudará a indexar las hojas de vida


def cambiarFotoSistema(event):
    global counterSystem
    if counterSystem == 3:
        # Foto del sistema 1
        labelFotoSystem.config(image=fotoSystem1)
        counterSystem = 1

    elif counterSystem == 1:
        # Foto del sistema 2
        labelFotoSystem.config(image=fotoSystem2)
        counterSystem = 2

    elif counterSystem == 2:
        # Foto del sistema 1
        labelFotoSystem.config(image=fotoSystem3)
        counterSystem = 3


labelFotoSystem.bind("<Enter>", cambiarFotoSistema)

# Boton de ingresar a la aplicacion
buttonP4 = Button(p4, text="Ingresar a la aplicación", command=lambda: abrirAplicacion())
buttonP4.pack(side="bottom", expand=True, fill='both')
buttonP4.config(bg='#193842', fg='white', font=('Arial', 13, 'bold'), padx=10, pady=8)

p2 = Frame(ventaPrincipal, bg="green")
p2.pack(side="right")

# Desarrolladores
p5 = Frame(p2)
p5.pack(expand=True, fill='both')

# Inicializacion de las hojas de vida / Jose Miguel
labelJose = Label(p2,
                  text="Jose Miguel Pulgarin Agudelo\n18 años\njpulgarina@unal.edu.co\nCarrera: Ingeniería de Sistemas e informática\nLema: 'Programar es muy bonito, \npero cuando las cosas funcionan :D'",
                  pady=4)
labelJose.pack(expand=True, fill='both')
labelJose.config(bg="#EBFDFF", fg="#213555", font=("Arial", 12))

# Dario Alexander
labelDario = Label(p2,
                   text="Dario Alexander Penagos Von Werde\n20 años\ndpenagosv@unal.edu.co\nCarrera: Ciencias de la computación\nLema: 'Estudiar es muy bonito, \npero cuando las cosas se entienden :D'",
                   pady=4)
labelDario.config(bg="#EBFDFF", fg="#213555", font=("Arial", 12))

# Carlos
labelCarlos = Label(p2,
                    text="Carlos Daniel Guarin Ortiz\n19 años\nCaguarin@unal.edu.co\nCarrera: ciencias de la computación\nLema:muy poquito tiempo, \npara la segunda entrega'",
                    pady=4)
labelCarlos.config(bg="#EBFDFF", fg="#213555", font=("Arial", 12))

# Fotos de los desarrolladores
p6 = Frame(p2)
p6.pack(side="bottom", expand=True, fill='both')

# fotos de Jose Miguel / Estas son inicializadas como las primeras en mostrarse
fotoJose1 = PhotoImage(file="versionEnPython/assets/jose.png").subsample(2, 2)
fotoJose2 = PhotoImage(file="versionEnPython/assets/jose2.png").subsample(2, 2)
fotoJose3 = PhotoImage(file="versionEnPython/assets/jose3.png").subsample(2, 2)
fotoJose4 = PhotoImage(file="versionEnPython/assets/jose4.png").subsample(2, 2)
labelFotoJose1 = Label(p6, image=fotoJose1)
labelFotoJose2 = Label(p6, image=fotoJose2)
labelFotoJose3 = Label(p6, image=fotoJose3)
labelFotoJose4 = Label(p6, image=fotoJose4)
labelFotoJose1.grid(row=0, column=0)
labelFotoJose2.grid(row=0, column=1)
labelFotoJose3.grid(row=1, column=0)
labelFotoJose4.grid(row=1, column=1)

# fotos de Dario Alexander
fotoDario1 = PhotoImage(file="versionEnPython/assets/Imagen_UnalDario.png").subsample(2, 2)
fotoDario2 = PhotoImage(file="versionEnPython/assets/il_nommeDario.png").subsample(2, 2)
fotoDario3 = PhotoImage(file="versionEnPython/assets/logo_UnalDario.png").subsample(2, 2)
fotoDario4 = PhotoImage(file="versionEnPython/assets/doggerDario.png").subsample(2, 2)
labelFotoDario1 = Label(p6, image=fotoDario1)
labelFotoDario2 = Label(p6, image=fotoDario2)
labelFotoDario3 = Label(p6, image=fotoDario3)
labelFotoDario4 = Label(p6, image=fotoDario4)

# fotos de Carlos
fotoCarlos1 = PhotoImage(file="versionEnPython/assets/carlos1.png").subsample(2, 2)
fotoCarlos2 = PhotoImage(file="versionEnPython/assets/carlos2.png").subsample(2, 2)
fotoCarlos3 = PhotoImage(file="versionEnPython/assets/carlos3.png").subsample(2, 2)
fotoCarlos4 = PhotoImage(file="versionEnPython/assets/carlos4.png").subsample(2, 2)
labelFotoCarlos1 = Label(p6, image=fotoCarlos1)
labelFotoCarlos2 = Label(p6, image=fotoCarlos2)
labelFotoCarlos3 = Label(p6, image=fotoCarlos3)
labelFotoCarlos4 = Label(p6, image=fotoCarlos4)

counterDev = 1  # Nos ayudará a indexar las hojas de vida


def cambiarHojaVida(event):
    global counterDev
    if counterDev == 3:
        # Habilitar a Jose Miguel
        # Label
        labelJose.pack(side='top', expand=True, fill='both')
        labelCarlos.pack_forget()  # Oculta el label de Carlos

        # Fotos
        labelFotoJose1.grid(row=0, column=0)
        labelFotoJose2.grid(row=0, column=1)
        labelFotoJose3.grid(row=1, column=0)
        labelFotoJose4.grid(row=1, column=1)
        # Ocultar fotos de Carlos
        labelFotoCarlos1.grid_forget()
        labelFotoCarlos2.grid_forget()
        labelFotoCarlos3.grid_forget()
        labelFotoCarlos4.grid_forget()

        counterDev = 1
    elif counterDev == 1:
        # Habilitar a Dario
        labelDario.pack(side="top", expand=True, fill='both')
        labelJose.pack_forget()  # Oculta el label de Jose

        # Fotos
        labelFotoDario1.grid(row=0, column=0)
        labelFotoDario2.grid(row=0, column=1)
        labelFotoDario3.grid(row=1, column=0)
        labelFotoDario4.grid(row=1, column=1)
        # Ocultar fotos de Jose
        labelFotoJose1.grid_forget()
        labelFotoJose2.grid_forget()
        labelFotoJose3.grid_forget()
        labelFotoJose4.grid_forget()

        counterDev = 2
    elif counterDev == 2:
        # Habilitar a Carlos
        labelCarlos.pack(side="top", expand=True, fill='both')
        labelDario.pack_forget()  # Oculta el label de Dario

        # Fotos
        labelFotoCarlos1.grid(row=0, column=0)
        labelFotoCarlos2.grid(row=0, column=1)
        labelFotoCarlos3.grid(row=1, column=0)
        labelFotoCarlos4.grid(row=1, column=1)
        # Ocultar fotos de Dario
        labelFotoDario1.grid_forget()
        labelFotoDario2.grid_forget()
        labelFotoDario3.grid_forget()
        labelFotoDario4.grid_forget()

        counterDev = 3


labelJose.bind("<Button-1>", cambiarHojaVida)
labelDario.bind("<Button-1>", cambiarHojaVida)
labelCarlos.bind("<Button-1>", cambiarHojaVida)

def cerrarAplicacionUsuario():
    root.deiconify()
    ventanaAplicacion.withdraw()

# Creación de la ventana de usuario
ventanaAplicacion = Toplevel(root)
ventanaAplicacion.title("Banco Nacho App")
ventanaAplicacion.iconbitmap("versionEnPython/assets/logo-unal.ico")  # Favicon de la apliación
ventanaAplicacion.withdraw()  # Ocultando la ventana hasta que sea llamada

ventanaAplicacion.protocol("WM_DELETE_WINDOW", cerrarAplicacionUsuario)

notebook = Notebook(ventanaAplicacion)
notebook.pack(pady=10, expand=True)

frameArchivo = Frame(notebook, width=400, height=280)
frameProcesos = Frame(notebook, width=400, height=280)
frameAyuda = Frame(notebook, width=400, height=280)

frameArchivo.pack(fill="both", expand=True)
frameProcesos.pack(fill="both", expand=True)
frameAyuda.pack(fill="both", expand=True)


# Pestaña de Ayuda
def mostrarAplicacion():
    messagebox.showinfo("¿Banco Nacho?", "Banco Nacho es una aplicación bancaria que permite a los clientes ver y realizar transacciones entre sus tarjetas bancarias. También pueden pagar facturas y convertir divisas. El programa ofrece 5 funcionalidades principales, incluyendo transacciones, pagos de facturas, conversión de divisas y deshacer transacciones.")


def SalirAInicio():
    root.deiconify()
    ventanaAplicacion.withdraw()


frameAr = Frame(frameArchivo)
frameAr.pack()
frameAr.config(width=400, height=280)

# Desplegar descripción de la aplicacion
LabelAplicacion = Label(frameAr, text="Conocer más acerca de la aplicación de Banco Nacho", pady=8)
LabelAplicacion.pack()
buttonAplicacion = Button(frameAr, text="Ver aplicación", command=lambda: mostrarAplicacion(), padx=10, pady=8)
buttonAplicacion.pack()

# Volver a la pantalla de inicio
LabelSalir = Label(frameAr, text="Volver a la pantalla principal", pady=8)
LabelSalir.pack()
buttonSalir = Button(frameAr, text="Salir", command=lambda: SalirAInicio(), padx=10, pady=8)
buttonSalir.pack()

# Pestaña de Ayuda
frameAy = Frame(frameAyuda)
frameAy.pack()
frameAy.config(width=400, height=280)


def mostrarAyuda():
    messagebox.showinfo("Desarrolladores", "Este proyecto ha sido desarrollado por:\n- Jose Miguel Pulgarin Agudelo\n- Dario Alexander Penagos Von Werde\n- Carlos Guarin")


LabelApicacion = Label(frameAy, text="Conocer más acerca de los creadores de Banco Nacho", pady=8)
LabelApicacion.pack()
buttonAplicacion = Button(frameAy, text="Ver desarrolladores", command=lambda: mostrarAyuda(), padx=10, pady=8)
buttonAplicacion.pack()


def procesoSolicitarTarjeta():
    """
    Esta función se encarga de la funcionalidad "solicitar tarjeta de crédito".
    Realiza un proceso para solicitar una tarjeta de crédito al cliente.
    """

    def segundoPaso():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Realiza las validaciones necesarias y muestra las tarjetas disponibles para el cliente.
        """

        # Validar que los campos relevantes estén completos
        if FF.getValores()[0] == "" or FF.getValores()[1] == "":
            messagebox.showinfo(title="Error", message="Por favor, ingrese los valores relevantes en todos los campos")
            return

        # Obtener el cliente actual y la divisa escogida
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        divisaEscogida = Divisa.encontrarDivisa(FF.getValores()[1])

        # Obtener el historial de créditos del cliente
        Historial = clienteActual.revisarHistorialDeCreditos()

        # Calcular el puntaje tentativo del cliente
        puntajeTentativo = Banco.calcularPuntaje(Historial)

        # Obtener las tarjetas bloqueadas y las tarjetas activas del cliente
        tarjetasBloqueadas = Tarjeta.TarjetasBloqueadas(clienteActual)
        tarjetasActivas = Tarjeta.TarjetasNoBloqueadas(clienteActual)

        # Modificar el puntaje definitivo del cliente
        puntajeDefinitivo = Factura.modificarPuntaje(tarjetasBloqueadas=tarjetasBloqueadas,
                                                     tarjetasActivas=tarjetasActivas, cliente=clienteActual,
                                                     puntaje=puntajeTentativo)

        # Obtener las tarjetas de crédito disponibles para el cliente con el puntaje definitivo y la divisa escogida
        tarjetasDisponibles = TarjetaCredito.tarjetasDisponibles(puntaje=puntajeDefinitivo, divisa=divisaEscogida)

        # Crear un nuevo frame para mostrar las tarjetas disponibles
        frameFinal = Frame(frameProcesos)
        Enunciado = Label(frameFinal, text="Escoga la tarjeta que quiere", padx=10, pady=10)
        Enunciado.grid(column=1, row=0)
        FF.forget()
        Buttons = []
        Labels = []

        def funcFinal(i: int):
            """
            Función que se ejecuta cuando se escoge una tarjeta. Añade la tarjeta de crédito al cliente.

            Args:
                i (int): Índice de la tarjeta escogida en la lista de tarjetas disponibles.
            """
            tarjetaEscogida = tarjetasDisponibles[i]
            TarjetaCredito.anadirTarjetaCredito(cliente=clienteActual, tarjeta=tarjetaEscogida)
            frameFinal.forget()
            frameP.tkraise()
            frameP.pack()
            return
        def funcRechazar():
            frameFinal.forget()
            frameP.pack()

        # Mostrar las tarjetas disponibles con sus respectivos botones de selección
        Label(frameFinal, text=tarjetasDisponibles[len(tarjetasDisponibles)-1], padx=10, pady=10).grid(column=0, row=2)
        Button(frameFinal, text="Aceptar", command=lambda: funcFinal(len(tarjetasDisponibles)-1)).grid(column=2, row=2, padx=10, pady=10)
        Button(frameFinal, text="Rechazar", command=lambda: funcRechazar()).grid(column=2, row=3, padx=10, pady=10)
        frameFinal.pack()

    # Obtener la lista de clientes y divisas para mostrar en los campos de selección
    clientes = Banco.getClientes()
    nomClientes = []
    for c in clientes:
        nomClientes.append(c.getNombre())
    nomDivisas = []
    for D in Divisa:
        nomDivisas.append(D.name)
    # Crear un FieldFrame para capturar los criterios del cliente y divisa de la tarjeta
    FF = FieldFrame(frameProcesos, "Criterios", ["Cliente", "Divisa de la tarjeta"], "Valores", segundoPaso, [nomClientes, nomDivisas])
    frameP.forget()
    FF.tkraise()
    FF.pack()

def procesoPagarFactura():
    """
    Esta función se encarga de la funcionalidad "Pagar factura".
    Realiza un proceso para que un cliente pague una factura.
    """

    def segundoPaso():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Realiza las validaciones necesarias y muestra los campos para completar el pago de la factura.
        """

        def ultimoPaso():
            """
            Función que se activa cuando el usuario oprime "Aceptar" en el último paso del proceso.
            Realiza las validaciones finales y realiza la transacción de pago de la factura.
            """

            # Validar que todos los campos relevantes estén completos
            if FF2.getValores()[0] == "" or FF2.getValores()[1] == "" or FF2.getValores()[2] == "":
                messagebox.showinfo(title="Error",
                                    message="Por favor, ingrese los valores relevantes en todos los campos")
                return

            # Obtener la factura y tarjeta escogida por el usuario
            facturaEscogida = clienteActual.encontrarFacturas(FF2.getValores()[0])
            tarjetaEscogida = clienteActual.encontrarTarjeta(FF2.getValores()[1])

            try:
                try:
                    monto = float(FF2.getValores()[2])  # Convertir el monto a un valor float
                except:
                    raise ErrorInput
            except ErrorInput as error:
                messagebox.showinfo(title="Error", message=error.__str__())
                return

            # Verificar que la tarjeta escogida sea válida para la transacción
            if not tarjetaEscogida in clienteActual.listarTarjetas(facturaEscogida):
                messagebox.showinfo(title="Error", message="La tarjeta escogida no es válida para esta transacción")
                return

            # Generar la transacción de pago de la factura
            transaccon = facturaEscogida.generarTransaccion(monto, tarjetaEscogida)

            if transaccon.rechazado:
                messagebox.showinfo(title="Error", message="La transacción ha sido rechazada")

            FF2.forget()
            frameP.pack()

        # Validar que el campo del usuario esté completo
        if FF.getValores()[0] == "":
            messagebox.showinfo(title="Error", message="Por favor, ingrese los valores relevantes en todos los campos")
            return

        # Obtener el cliente actual y sus facturas y tarjetas asociadas
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        facturas = clienteActual.mostrarFacturas()
        tarjetas = clienteActual.getTarjetas()

        try:
            FF2 = FieldFrame(frameProcesos, "", ["Seleccione la factura", "Seleccione la tarjeta",
                                                 "Ingrese la cantidad de dinero que desea transferir"], "", ultimoPaso,
                             [facturas, tarjetas, None])
        except ErrorOpciones as error:
            messagebox.showinfo(title="Error", message=error.__str__())
            return

        FF.forget()
        FF2.pack()

    # Obtener la lista de clientes para mostrar en el campo de selección
    clientes = Banco.getClientes()

    # Crear un FieldFrame para capturar el cliente que desea pagar una factura
    FF = FieldFrame(frameProcesos, "", ["Seleccione el usuario"], "", segundoPaso, [[c.getNombre() for c in clientes]])
    frameP.forget()
    FF.pack()


def procesoHacerTransaccion():
    """
    Esta función se encarga de la funcionalidad "Hacer transacción".
    Realiza un proceso para que un cliente realice una transacción a otro cliente.
    """

    def paso2():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Realiza las validaciones necesarias y muestra los campos para completar la transacción.
        """

        def paso3():
            """
            Función que se activa cuando el usuario oprime "Aceptar" en el tercer paso del proceso.
            Realiza las validaciones necesarias y muestra los campos para completar la transacción.
            """

            def ultimoPaso():
                """
                Función que se activa cuando el usuario oprime "Aceptar" en el último paso del proceso.
                Realiza las validaciones finales y crea la transacción entre los clientes.
                """

                try:
                    tarjetaObjetivo = clienteObjetivo.encontrarTarjeta(FF3.getValores()[0])
                    monto = float(FF3.getValores()[1])  # Convertir el monto a un valor float
                except ErrorOpciones as error:
                    messagebox.showinfo(title="Error", message=error.__str__())
                    return
                except error:
                    messagebox.showinfo(title="Error", message="Por favor, ingrese una cantidad válida")
                    return

                # Crear la transacción entre los clientes
                trans = Transaccion(clienteActual, clienteObjetivo, tarjetaEscogida, tarjetaObjetivo, monto)

                if trans.rechazado:
                    if monto > tarjetaEscogida.getSaldo():
                        messagebox.showinfo(title="Error",
                                            message="La transacción ha fallado porque no hay suficiente dinero en la cuenta")
                        FF3.forget()
                        frameP.pack()
                        return
                    elif not tarjetaEscogida.getDivisa() == tarjetaObjetivo.getDivisa():
                        messagebox.showinfo(title="Error", message="Error: las tarjetas no tienen la misma divisa")
                        FF3.forget()
                        frameP.pack()
                        return

                FF3.forget()
                frameP.pack()

            # Obtener el cliente objetivo y la tarjeta escogida por el cliente actual
            clienteObjetivo = Banco.encontrarCliente(FF2.getValores()[0])
            tarjetaEscogida = clienteActual.encontrarTarjeta(FF2.getValores()[1])

            try:
                FF3 = FieldFrame(frameProcesos, "", ["Tarjeta que recibe la transacción", "Monto a transferir"], "",
                                 ultimoPaso, [[t.__str__() for t in clienteObjetivo.getTarjetasDebito()], None])
            except ErrorOpciones as error:
                messagebox.showinfo(title="Error", message=error.__str__())
                FF2.forget()
                frameP.pack()
                return

            FF3.pack()
            FF2.forget()

        # Obtener el cliente actual
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])

        try:
            FF2 = FieldFrame(frameProcesos, "", ["Usuario que recibe la transacción", "Tarjeta de origen"], "", paso3,
                             [[c.getNombre() for c in Banco.getClientes()],
                              [t.__str__() for t in clienteActual.getTarjetas()]])
        except ErrorOpciones as error:
            messagebox.showinfo(title="Error", message=error.__str__())
            FF.forget()
            frameP.pack()
            return

        FF2.pack()
        FF.forget()

    # Mostrar el campo para seleccionar el cliente que hace la transacción
    FF = FieldFrame(frameProcesos, "", ["Usuario que hace la transacción"], "", paso2,
                    [[c.getNombre() for c in Banco.getClientes()]])
    FF.pack()
    frameP.forget()


def procesoDeshacerTransaccion():
    """
    Esta función se encarga de la funcionalidad "Deshacer transacción".
    Realiza un proceso para que un cliente solicite deshacer una transacción realizada previamente.
    """

    def paso2():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Realiza las validaciones necesarias y muestra los campos para completar la solicitud de deshacer transacción.
        """

        def pasoFinal():
            """
            Función que se activa cuando el usuario oprime "Aceptar" en el último paso del proceso.
            Realiza las validaciones finales y genera una petición para deshacer la transacción seleccionada.
            """

            # Obtener la transacción a deshacer y el mensaje de solicitud ingresado
            transaccionADeshacer = Transaccion.encontrarTransaccion(FF2.getValores()[0])
            mensaje = FF2.getValores()[1]

            # Generar la petición para deshacer la transacción
            Banco.generarPeticion(transaccionADeshacer, mensaje)

            FF2.forget()
            frameP.pack()

        # Obtener el cliente actual
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])

        try:
            FF2 = FieldFrame(frameProcesos, "", ["Escoga la transacción que desea deshacer","Ingrese el mensaje que desea que vea el cliente que recibió la transacción"], "", pasoFinal, [Transaccion.encontrar_transacciones(clienteActual), None])
        except ErrorOpciones as error:
            messagebox.showinfo(title="Error", message=error.__str__())
            FF.forget()
            frameP.pack()
            return

        FF.forget()
        FF2.pack()

    # Mostrar el campo para seleccionar el cliente que realiza la solicitud de deshacer transacción
    FF = FieldFrame(frameProcesos, "", ["Escoga el cliente"], "", paso2, [[c.getNombre() for c in Banco.getClientes()]])
    frameP.forget()
    FF.pack()



def procesoVerPeticiones():
    """
    Esta función se encarga de la funcionalidad "Ver peticiones".
    Permite al cliente ver las peticiones que hay a su nombre y conceder o negarlas según corresponda.
    """

    def paso2():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Muestra las peticiones pendientes del cliente seleccionado y permite al cliente conceder o negar cada una.
        """

        def funcBotones(i):
            """
            Función que se activa cuando se hace clic en una transacción específica.
            Muestra los detalles de la transacción y los botones para conceder o negar la petición.
            """

            def funcVolver():
                """
                Función que se activa cuando se hace clic en el botón "Volver".
                Vuelve a la pantalla principal del proceso.
                """
                labelPrincipal.destroy()
                labelTransaccion.destroy()
                botonVolver.destroy()
                botonNegar.destroy()
                botonConceder.destroy()
                frameP.pack()

            def funcNegar():
                """
                Función que se activa cuando se hace clic en el botón "Negar la petición".
                Niega la petición y actualiza el estado de la transacción correspondiente.
                """
                transNueva = Transaccion.completarTransaccion(transaccion=transaccionActual, respuesta=False)
                Transaccion.getTransacciones()[Transaccion.getTransacciones().index(transaccionActual)] = transNueva
                labelPrincipal.destroy()
                labelTransaccion.destroy()
                botonVolver.destroy()
                botonNegar.destroy()
                botonConceder.destroy()
                frameP.pack()

            def funcAceptar():
                """
                Función que se activa cuando se hace clic en el botón "Conceder la petición".
                Concede la petición y actualiza el estado de la transacción correspondiente.
                """
                transNueva = Transaccion.completarTransaccion(transaccion=transaccionActual, respuesta=True)
                Transaccion.getTransacciones()[Transaccion.getTransacciones().index(transaccionActual)] = transNueva
                labelPrincipal.destroy()
                labelTransaccion.destroy()
                botonVolver.destroy()
                botonNegar.destroy()
                botonConceder.destroy()
                frameP.pack()

            enunciado.forget()
            for w in frameProcesos.winfo_children():
                w.forget()

            # Obtener la transacción actual
            transaccionActual = transacciones[i]

            # Mostrar los detalles de la transacción y los botones para conceder o negar la petición
            labelPrincipal = Label(frameProcesos, text="Escoga lo que quiere hacer con la siguiente transacción",
                                   padx=10, pady=10)
            labelPrincipal.grid(column=1, row=0)
            labelTransaccion = Label(frameProcesos, text=transaccionActual, padx=10, pady=10)
            labelTransaccion.grid(column=1, row=1)
            botonVolver = Button(frameProcesos, text="Volver", command=lambda: funcVolver(), padx=10, pady=10)
            botonVolver.grid(column=0, row=3)
            botonNegar = Button(frameProcesos, text="Negar la petición", padx=10, pady=10,
                                command=lambda: funcNegar())
            botonNegar.grid(column=1, row=3)
            botonConceder = Button(frameProcesos, text="Conceder la petición", padx=10, pady=10,
                                   command=lambda: funcAceptar())
            botonConceder.grid(column=2, row=3)

        # Obtener el cliente actual
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])

        # Obtener las transacciones pendientes del cliente
        transacciones = clienteActual.verPeticiones()

        if len(transacciones) == 0:
            messagebox.showinfo(title="Error", message="El cliente escogido no tiene peticiones actualmente")
            FF.forget()
            frameP.pack()
            return
        botones = []
        FF.forget()
        enunciado = Label(frameProcesos, text="Escoga la transacción que quiere tratar, o 'volver' para salir")
        enunciado.pack()

        # Mostrar botones para cada transacción pendiente
        for i in range(len(transacciones)):
            botones.append(
                Button(frameProcesos, text=transacciones[i], padx=10, pady=100, command=lambda: funcBotones(i)).pack())

    # Mostrar el campo para seleccionar el cliente cuyas peticiones se desea ver
    FF = FieldFrame(frameProcesos, "", ["Escoga el usuario cuyas peticiones desea ver"], "", paso2,
                    [[c.getNombre() for c in Banco.getClientes()]])
    FF.pack()
    frameP.forget()



def procesoCambiarDivisa():
    """
    Esta función se encarga del proceso de cambiar divisas.
    Permite al cliente realizar una conversión de divisas entre dos tarjetas y a través de un canal específico.
    """

    def paso2():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Permite al cliente ingresar los detalles de la transacción, como las tarjetas y la cantidad a transferir.
        """

        def pasoFinal():
            """
            Función que se activa cuando el usuario oprime "Aceptar" en el último paso del proceso.
            Realiza la conversión de divisas y genera una transacción correspondiente.
            """

            tarjetaOrigen = clienteActual.encontrarTarjeta(FF2.getValores()[0])
            tarjetaObjetivo = clienteActual.encontrarTarjeta(FF2.getValores()[1])
            canalEscogido = Banco.encontrarCanal(FF2.getValores()[3])

            try:
                try:
                    cantidad = float(FF2.getValores()[2])
                except:
                    raise ErrorInput
            except ErrorInput as error:
                messagebox.showinfo(title="Error", message=error.__str__())
                return

            conversion = Divisa.convertir_divisas([divisaOrigen, divisaObjetivo], canalEscogido, cantidad)
            transaccion = Transaccion.crearTransaccion([divisaOrigen, divisaObjetivo], cantidad, conversion,
                                                       canalEscogido, [tarjetaOrigen, tarjetaObjetivo], clienteActual)
            transaccion = canalEscogido.finalizarConversion(transaccion, cantidad)
            if transaccion.rechazado:
                messagebox.showinfo(title="Error", message="La operación no se pudo completar")
            FF2.forget()
            frameP.pack()

        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        divisaOrigen = Divisa.encontrarDivisa(FF.getValores()[1])
        divisaObjetivo = Divisa.encontrarDivisa(FF.getValores()[2])

        if divisaOrigen == divisaObjetivo:
            messagebox.showinfo(title="Error", message="Debe escoger dos divisas diferentes para hacer la conversión")
            return

        tarjetasOrigen = clienteActual.tarjetasConDivisa(divisaOrigen, True)
        tarjetasObjetivo = clienteActual.tarjetasConDivisa(divisaObjetivo, False)

        if len(tarjetasOrigen) == 0 or len(tarjetasObjetivo) == 0:
            messagebox.showinfo(title="Error",
                                message="El cliente escogido no dispone de tarjetas que pueda utilizar en esta operación")
            return

        canalesDisponibles = clienteActual.listarCanales(divisaOrigen, divisaObjetivo)

        if len(canalesDisponibles) == 0:
            messagebox.showinfo(title="Error", message="No hay canales disponibles para esta operación")
            return

        FF2 = FieldFrame(frameProcesos, "", ["Tarjeta de la que saldrá el dinero",
                                             "Tarjeta que recibe el dinero",
                                             "Monto total a transferir (en las unidades de la divisa original)",
                                             "Canal mediante el cual desea hacer la transacción"], "", pasoFinal,
                         [tarjetasOrigen, tarjetasObjetivo, None, canalesDisponibles])

        FF2.pack()
        FF.forget()

    # Mostrar el campo para seleccionar el cliente que realiza la conversión y las divisas involucradas
    FF = FieldFrame(frameProcesos, "", ["Cliente que hace la conversión", "Divisa que desea convertir",
                                        "Divisa que desea recibir"], "", paso2,
                    [[c.nombre for c in Banco.getClientes()], [d.name for d in Divisa], [d.name for d in Divisa]])

    frameP.forget()
    FF.pack()

def procesoRetirarODepositarDinero():
    """
    Esta función se encarga del proceso de retirar o depositar dinero.
    Permite al cliente seleccionar una tarjeta y un canal para realizar la operación.
    """

    def segundoPaso():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Permite al cliente ingresar los detalles de la transacción, como la tarjeta y el monto a retirar o depositar.
        """

        def pasoFinal():
            """
            Función que se activa cuando el usuario oprime "Aceptar" en el último paso del proceso.
            Realiza la transacción de retirar o depositar dinero y finaliza la transacción correspondiente.
            """

            # Obtener la tarjeta y el canal seleccionados por el usuario
            tarjetaEscogida = clienteActual.encontrarTarjeta(FF2.getValores()[0])
            canalEscogido = Banco.encontrarCanal(FF2.getValores()[1])

            try:
                # Convertir el monto ingresado a un número float
                monto = float(FF2.getValores()[2])
            except ValueError:
                # Mostrar mensaje de error si el monto ingresado no es un número válido
                messagebox.showinfo(title="Error", message="Por favor, ingrese un número válido")
                return

            # Crear la transacción inicial y verificar si es rechazada
            transaccionInicial = Transaccion.crearTrans(clienteActual, tarjetaEscogida, monto, canalEscogido, retirar)

            if transaccionInicial.rechazado:
                if not tarjetaEscogida.puedeTransferir(monto):
                    # Mostrar mensaje de error si la tarjeta no puede transferir el monto necesario
                    messagebox.showinfo(title="Error", message="La tarjeta no puede transferir el monto necesario")
                    FF2.forget()
                    frameP.pack()
                    return

                if canalEscogido.getFondos(divisaEscogida) < monto:
                    # Mostrar mensaje de error si el canal no tiene suficientes fondos para la acción
                    messagebox.showinfo(title="Error", message="El canal no tiene suficientes fondos para esta acción")
                    FF2.forget()
                    frameP.pack()
                    return

                return

            # Finalizar la transacción y mostrar el frame principal
            transaccionFinal = Canal.finalizarTransaccion(transaccionInicial, retirar)
            FF2.forget()
            frameP.pack()

        # Obtener el cliente seleccionado y si desea retirar o depositar dinero
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        retirar = False
        if FF.getValores()[1] == "Retirar":
            retirar = True

        # Obtener la divisa seleccionada
        divisaEscogida = Divisa.encontrarDivisa(FF.getValores()[2])

        if divisaEscogida not in Banco.seleccionarDivisa(clienteActual):
            # Mostrar mensaje de error si el cliente no puede realizar la operación con la divisa seleccionada
            messagebox.showinfo(title="Error", message="El cliente seleccionado no puede realizar esta operación con la divisa escogida")
            return

        # Obtener las tarjetas y los canales disponibles para la operación
        tarjetas = clienteActual.seleccionarTarjeta(divisaEscogida, retirar)
        canales = Canal.seleccionarCanal(divisaEscogida, retirar)

        if len(tarjetas) == 0:
            # Mostrar mensaje de error si el cliente no tiene tarjetas para realizar la operación
            messagebox.showinfo(title="Error", message="El cliente seleccionado no tiene tarjetas que puedan realizar esta operación")
            return

        if len(canales) == 0:
            # Mostrar mensaje de error si no hay canales disponibles para la operación
            messagebox.showinfo(title="Error", message="No hay canales disponibles para realizar esta transacción")
            return

        try:
            # Mostrar el campo para seleccionar la tarjeta, el canal y el monto
            FF2 = FieldFrame(frameProcesos, "", ["Escoga la tarjeta mediante la cual desea hacer la operación",
                                                "Escoga el canal que desea utilizar",
                                                "Escoga el monto total"], "", pasoFinal,
                            [[t for t in tarjetas if retirar or isinstance(t, TarjetaDebito)], canales, None])
        except ErrorOpciones as error:
            # Mostrar mensaje de error si hay un problema con las opciones disponibles
            messagebox.showinfo(title="Error", message=error.__str__())
            FF.forget()
            frameP.pack()

        FF2.pack()
        FF.forget()

    # Mostrar el campo para seleccionar el cliente, si desea retirar o depositar y la divisa
    FF = FieldFrame(frameProcesos, "", ["Cliente que realiza la operación", "Acción a realizar", "Divisa"], "", segundoPaso,
                    [[c.nombre for c in Banco.getClientes()], ["Retirar", "Depositar"], [d.name for d in Divisa]])

    frameP.forget()
    FF.pack()

    FF = FieldFrame(frameProcesos, "", ["Elija el usuario", "¿Quiere depositar o retirar?",
                                        "Seleccione la divisa con la cual quiere realizar la operación"], "", segundoPaso,
                    [[c.nombre for c in Banco.getClientes()], ["Retirar", "Depositar"], [d.name for d in Divisa]])

    FF.pack()
    frameP.forget()


def procesoVerTarjetas():
    """
    Esta función se encarga del proceso de visualizar las tarjetas de un cliente.
    Permite al usuario seleccionar un cliente y muestra las tarjetas asociadas a ese cliente.
    """

    def pasoDos():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Muestra las tarjetas asociadas al cliente seleccionado.
        """

        def funcVolver():
            """
            Función que se activa cuando el usuario oprime "Volver".
            Regresa al paso anterior del proceso.
            """
            frameInfo.forget()
            frameP.pack()

        # Obtener el cliente seleccionado
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])

        # Obtener las tarjetas asociadas al cliente
        tarjetas = clienteActual.getTarjetas()

        FF.forget()

        # Mostrar título de las tarjetas del cliente seleccionado
        labelPrincipal = Label(frameInfo, text="Tarjetas del usuario escogido")
        labelPrincipal.grid(row=0, column=1, pady=10)

        labels = []
        for i in range(len(tarjetas)):
            # Mostrar cada tarjeta en un Label separado
            labels.append(Label(frameInfo, text=tarjetas[i].__str__()))
            labels[i].grid(column=i % 3, row=math.floor(i / 3) + 1)

        # Agregar botón "Volver" para regresar al paso anterior
        botonVolver = Button(frameInfo, text="Volver", command=lambda: funcVolver())
        botonVolver.grid(column=1, row=math.floor(len(tarjetas) / 3) + 3)

    frameInfo = Frame(frameProcesos)
    frameInfo.pack()
    frameP.forget()

    # Mostrar el campo para seleccionar el cliente
    FF = FieldFrame(frameInfo, "", ["Escoga el cliente cuyas tarjetas desea ver"], "", pasoDos,
                    [[c.nombre for c in Banco.getClientes()]])
    FF.pack()



def procesoVerFacturas():
    """
    Esta función se encarga del proceso de visualizar las facturas de un cliente.
    Permite al usuario seleccionar un cliente y muestra las facturas asociadas a ese cliente.
    """

    def pasoDos():
        """
        Función que se activa cuando el usuario oprime "Aceptar" en el segundo paso del proceso.
        Muestra las facturas asociadas al cliente seleccionado.
        """

        def funcVolver():
            """
            Función que se activa cuando el usuario oprime "Volver".
            Regresa al paso anterior del proceso.
            """
            frameInfo.forget()
            frameP.pack()

        # Obtener el cliente seleccionado
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])

        # Obtener las facturas asociadas al cliente
        facturas = clienteActual.getFacturas()

        FF.forget()

        # Mostrar título de las facturas del cliente seleccionado
        labelPrincipal = Label(frameInfo, text="Facturas del usuario escogido")
        labelPrincipal.grid(row=0, column=1, pady=10)

        labels = []
        for i in range(len(facturas)):
            # Mostrar cada factura en un Label separado
            labels.append(Label(frameInfo, text=facturas[i].__str__()))
            labels[i].grid(column=i % 3, row=math.floor(i / 3) + 1)

        # Agregar botón "Volver" para regresar al paso anterior
        botonVolver = Button(frameInfo, text="Volver", command=lambda: funcVolver())
        botonVolver.grid(column=1, row=math.floor(len(facturas) / 3) + 3)

    frameInfo = Frame(frameProcesos)
    frameInfo.pack()
    frameP.forget()

    # Mostrar el campo para seleccionar el cliente
    FF = FieldFrame(frameInfo, "", ["Escoga el cliente cuyas facturas desea ver"], "", pasoDos,
                    [[c.nombre for c in Banco.getClientes()]])
    FF.pack()


# Pestana de Procesos y consultas
frameP = Frame(frameProcesos)

BsolicitarTarjeta = Button(frameP, text="Solicitar tarjeta", command=lambda: procesoSolicitarTarjeta(), padx=10, pady=10)
BpagarFactura = Button(frameP, text="Pagar factura", command=lambda: procesoPagarFactura(), padx=10, pady=10)
BhacerTransaccion = Button(frameP, text="Hacer transaccion", command=lambda: procesoHacerTransaccion(), padx=10, pady=10)
BdeshacerTransaccion = Button(frameP, text="Deshacer transaccion", command=lambda: procesoDeshacerTransaccion(), padx=10, pady=10)
BverPeticiones = Button(frameP, text="Ver peticiones", command=lambda: procesoVerPeticiones(), padx=10, pady=10)
BCambiarDivisa = Button(frameP, text="Cambiar Divisas", command=lambda: procesoCambiarDivisa(), padx=10, pady=10)
BRetirarODepositar = Button(frameP, text="Retirar o depositar dinero", command=lambda: procesoRetirarODepositarDinero(), padx=10, pady=10)
BVerTarjetas = Button(frameP, text="Ver tarjetas de un cliente", command=lambda: procesoVerTarjetas())
BVerFacturas = Button(frameP, text="Ver facturas de un cliente", command=lambda: procesoVerFacturas())

label1 = Label(frameP, text="Funcionalidades", padx = 20, pady=20)
label2 = Label(frameP, text="Servicios", padx = 20, pady=20)

label1.grid(column=0, row=0)
label2.grid(column=1, row=0)

BpagarFactura.grid(column = 0, row = 1)
BCambiarDivisa.grid(column=0, row=2)
BRetirarODepositar.grid(column=0, row=3)
BsolicitarTarjeta.grid(column=0, row=4)
BdeshacerTransaccion.grid(column=0, row=5)
BverPeticiones.grid(column=0, row=6)


BhacerTransaccion.grid(column=1, row=1)
BVerTarjetas.grid(column=1, row=2)
BVerFacturas.grid(column=1, row=3)

frameP.pack()

notebook.add(frameArchivo, text="Archivo")
notebook.add(frameProcesos, text="Procesos y Consultas")
notebook.add(frameAyuda, text="Ayuda")

root.mainloop()