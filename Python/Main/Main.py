import tkinter as tk
from tkinter.ttk import Notebook
from tkinter import ttk, messagebox, Frame, Button, Label
from FieldFrame import FieldFrame
from Banco import Banco
from Cliente import Cliente
from Divisa import Divisa
from Tarjeta import Tarjeta
from TarjetaCredito import TarjetaCredito
from TarjetaDebito import TarjetaDebito
from Factura import Factura
from Transaccion import Transaccion

def setup():
    cliente1 = Cliente("Carlos", 555)
    cliente2 = Cliente("Carolina", 777)
    cliente3 = Cliente("José", 888)
    TarjetaDeb1 = TarjetaDebito(1, Divisa.EURO, 100000)
    TarjetaDeb2 = TarjetaDebito(2, Divisa.DOLAR, 100000)
    TarjetaDeb3 = TarjetaDebito(3, Divisa.DOLAR, 100000)

    TarjetaCred1 = TarjetaCredito(4, Divisa.DOLAR, 10000, 10)

    tarjetaFac = TarjetaDebito(666, Divisa.DOLAR, 100)
    factura1 = Factura(cliente1, 100, 5, tarjetaFac)

    cliente1.agregarTarjetasDebito(TarjetaDeb1, TarjetaDeb2)
    cliente1.agregarTarjetasCredito(TarjetaCred1)
    cliente3.agregarTarjetasDebito(TarjetaDeb3)

setup()

root = tk.Tk()

root.title("Banco Nacho")

notebook = Notebook(root)
notebook.pack(pady = 10, expand=True)



frameArchivo = Frame(notebook, width=400, height=280)
frameProcesos = Frame(notebook, width=400, height=280)
frameAyuda = Frame(notebook, width=400, height=280)

frameArchivo.pack(fill="both", expand=True)
frameProcesos.pack(fill="both", expand=True)
frameAyuda.pack(fill="both", expand=True)

#Configurando la pestaña de Ayuda
frameA = Frame(frameArchivo)
frameA.pack()
frameA.config(width=400,height=280) 

def mostrarAplicacion():
    messagebox.showinfo("¿Banco Nacho?", "Banco Nacho es una aplicación bancaria que permite a los clientes ver y realizar transacciones entre sus tarjetas bancarias. También pueden pagar facturas y convertir divisas. El programa ofrece 5 funcionalidades principales, incluyendo transacciones, pagos de facturas, conversión de divisas y deshacer transacciones.")
LabelApicacion = Label(frameA, text="Conocer más acerca de la aplicación de Banco Nacho", pady=8)
LabelApicacion.pack()
buttonAplicacion = Button(frameA, text="Ver aplicación", command=lambda: mostrarAplicacion(), padx= 10, pady=8)
buttonAplicacion.pack()

frameP = Frame(frameProcesos)
frameP.pack()


def procesoSolicitarTarjeta(): # Esta función se encarga de la funcionalidad "solicitar tarjeta de crédito"
    def segundoPaso(): # Función que se activa cuando el usuario oprime "Aceptar"
        if FF.getValores()[0] == "" or FF.getValores()[1] == "":
            messagebox.showinfo(title="Error", message="Por favor, ingrese los valores relevantes en todos los campos")
            return
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        divisaEscogida = Divisa.encontrarDivisa(FF.getValores()[1])
        Historial = clienteActual.revisarHistorialDeCreditos()
        puntajeTentativo = Banco.calcularPuntaje(Historial)
        tarjetasBloqueadas = Tarjeta.TarjetasBloqueadas(clienteActual)
        tarjetasActivas = Tarjeta.TarjetasNoBloqueadas(clienteActual)
        puntajeDefinitivo = Factura.modificarPuntaje(tarjetasBloqueadas=tarjetasBloqueadas, tarjetasActivas= tarjetasActivas, cliente=clienteActual, puntaje=puntajeTentativo)
        tarjetasDisponibles = TarjetaCredito.tarjetasDisponibles(puntaje=puntajeDefinitivo, divisa=divisaEscogida)
        frameFinal = Frame(frameProcesos)
        Enunciado = Label(frameFinal, text="Escoga la tarjeta que quiere", padx=10, pady=10)
        Enunciado.grid(column=1, row=0)
        FF.forget()
        Buttons = []
        Labels = []
        def funcFinal(i: int):
            tarjetaEscogida = tarjetasDisponibles[i]
            TarjetaCredito.anadirTarjetaCredito(cliente=clienteActual, tarjeta=tarjetaEscogida)
            frameFinal.forget()
            frameP.tkraise()
            frameP.pack()
        for i in range(len(tarjetasDisponibles)):
            Labels.append(Label(frameFinal, text=tarjetasDisponibles[i], padx=10, pady=10).grid(column=0, row= i+3))
            Buttons.append(Button(frameFinal, text="Escoger", command= lambda: funcFinal(i)).grid(column=2, row = i+3, padx=10, pady=10))
        frameFinal.pack()
    clientes = Banco.getClientes()
    nomClientes = []
    for c in clientes:
        nomClientes.append(c.getNombre())
    nomDivisas = []
    for D in Divisa:
        nomDivisas.append(D.name)
    FF = FieldFrame(frameProcesos, "Criterios", ["Cliente", "Divisa de la tarjeta"], "Valores", segundoPaso, [nomClientes, nomDivisas])
    frameP.forget()
    FF.tkraise()
    FF.pack()
    
def procesoPagarFactura(): # Esta función se encarga de la funcionalidad "Pagar factura"
    def segundoPaso():
        def ultimoPaso():
            if FF2.getValores()[0] == "" or FF2.getValores()[1] == "" or FF2.getValores()[2] == "":
                messagebox.showinfo(title="Error", message="Por favor, ingrese los valores relevantes en todos los campos")
                return
            facturaEscogida = clienteActual.encontrarFacturas(FF2.getValores()[0])
            tarjetaEscogida = clienteActual.encontrarTarjeta(FF2.getValores()[1])
            try:
                monto = float(FF2.getValores()[2])
            except:
                messagebox.showinfo(title="Error", message="Por favor, ingrese una cantidad de dinero válida")
                return
            if not tarjetaEscogida in clienteActual.listarTarjetas(facturaEscogida):
                messagebox.showinfo(title="Error", message="La tarjeta escogida no es válida para esta transacción")
                return
            transaccon = facturaEscogida.generarTransaccion(monto, tarjetaEscogida)
            if transaccon.rechazado:
                messagebox.showinfo(title="Error", message="La transacción ha sido rechazada")
            FF2.forget()
            frameP.pack()
        if FF.getValores()[0] == "":
            messagebox.showinfo(title="Error", message="Por favor, ingrese los valores relevantes en todos los campos")
            return
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        facturas = clienteActual.mostrarFacturas()
        tarjetas = clienteActual.getTarjetas()
        try:
            FF2 = FieldFrame(frameProcesos, "", ["Seleccione la factura", "Seleccione la tarjeta", "Ingrese la cantidad de dinero que desea transferir"], "", ultimoPaso, [facturas, tarjetas, None])
        except TypeError:
            messagebox.showinfo(title="Error", message="Usted no tiene facturas disponibles por pagar")
            return
        FF.forget()
        FF2.pack()
    clientes = Banco.getClientes()
    FF = FieldFrame(frameProcesos, "", ["Seleccione el usuario"], "", segundoPaso, [[c.getNombre() for c in clientes]])
    frameP.forget()
    FF.pack()
    
def procesoHacerTransaccion(): #Se encarga de crear una transacción entre usuarios
    def paso2():
        def paso3():
            def ultimoPaso():
                try:
                    tarjetaObjetivo = clienteObjetivo.encontrarTarjeta(FF3.getValores()[0])
                    monto = float(FF3.getValores()[1])
                except ValueError:
                    messagebox.showinfo(title="Error", message="Por favor, ingrese una cantidad válida")
                    return
                trans = Transaccion(clienteActual, clienteObjetivo, tarjetaEscogida, tarjetaObjetivo, monto)
                if trans.rechazado:
                    if monto > tarjetaEscogida.getSaldo():
                        messagebox.showinfo(title="Error", message="La transacción ha fallado porque no hay suficiente dinero en la cuenta")
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
            clienteObjetivo = Banco.encontrarCliente(FF2.getValores()[0])
            tarjetaEscogida = clienteActual.encontrarTarjeta(FF2.getValores()[1])
            try:
                FF3 = FieldFrame(frameProcesos, "", ["Tarjeta que recibe la transacción", "Monto a transferir"],"", ultimoPaso, [[t.__str__() for t in clienteObjetivo.getTarjetasDebito()], None])
            except TypeError:
                messagebox.showinfo(title="Error", message="El cliente escogido no dispone de tarjetas que puedan recibir esta transacción")
                FF2.forget()
                frameP.pack()
                return
            FF3.pack()
            FF2.forget()
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        try:
            FF2 = FieldFrame(frameProcesos, "", ["Usuario que recibe la transacción", "Tarjeta de origen"], "", paso3, [[c.getNombre() for c in Banco.getClientes()], [t.__str__() for t in clienteActual.getTarjetas()]])
        except TypeError:
                messagebox.showinfo(title="Error", message="El cliente escogido no dispone de tarjetas de las que pueda salir la transacción")
                FF.forget()
                frameP.pack()
                return
        FF2.pack()
        FF.forget()
    FF = FieldFrame(frameProcesos, "", ["Usuario que hace la transacción"], "", paso2, [[c.getNombre() for c in Banco.getClientes()]])
    FF.pack()
    frameP.forget()

def procesoDeshacerTransaccion(): #Se encarga de crear una petición para la funcionalidad deshacer transacción
    def paso2():
        def pasoFinal():
            transaccionADeshacer = Transaccion.encontrarTransaccion(FF2.getValores()[0])
            mensaje = FF2.getValores()[1]
            Banco.generarPeticion(transaccionADeshacer, mensaje)
            FF2.forget()
            frameP.pack()
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
        try:
            FF2 = FieldFrame(frameProcesos, "", ["Escoga la tranacción que desea deshacer", "Ingrese el mensaje que desea que vea el cliente que recibió la transacción"], "", pasoFinal, [Transaccion.encontrar_transacciones(clienteActual), None])
        except TypeError:
            messagebox.showinfo(title="Error", message="El cliente escogido no dispone de transferencias que se puedan deshacer")
            FF.forget()
            frameP.pack()
            return
        FF.forget()
        FF2.pack()
    FF = FieldFrame(frameProcesos, "", ["Escoga el cliente"], "", paso2, [[c.getNombre() for c in Banco.getClientes()]])
    frameP.forget()
    FF.pack()

def procesoVerPeticiones():
    def paso2():
        def funcBotones(i):
            def funcVolver():
                labelPrincipal.destroy()
                labelTransaccion.destroy()
                botonVolver.destroy()
                botonNegar.destroy()
                botonConceder.destroy()
                frameP.pack()
            def funcNegar():
                transNueva = Transaccion.completarTransaccion(transaccion=transaccionActual, respuesta=False)
                Transaccion.getTransacciones()[Transaccion.getTransacciones().index(transaccionActual)] = transNueva
                labelPrincipal.destroy()
                labelTransaccion.destroy()
                botonVolver.destroy()
                botonNegar.destroy()
                botonConceder.destroy()
                frameP.pack()
            def funcAceptar():
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
            transaccionActual = transacciones[i]
            labelPrincipal = Label(frameProcesos, text="Escoga lo que quiere hacer con la siguiente transacción", padx=10, pady=10)
            labelPrincipal.grid(column=1, row=0)
            labelTransaccion = Label(frameProcesos, text=transaccionActual, padx=10, pady=10)
            labelTransaccion.grid(column=1, row=1)
            botonVolver = Button(frameProcesos, text="Volver", command=lambda: funcVolver(), padx=10, pady=10)
            botonVolver.grid(column=0, row=3)
            botonNegar = Button(frameProcesos, text="Negar la petición", padx=10, pady=10, command=lambda: funcNegar())
            botonNegar.grid(column=1, row=3)
            botonConceder = Button(frameProcesos, text="Conceder la petición", padx=10, pady=10, command=lambda: funcAceptar())
            botonConceder.grid(column=2, row=3)
            
        clienteActual = Banco.encontrarCliente(FF.getValores()[0])
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
        for i in range(len(transacciones)):
            botones.append(Button(frameProcesos, text=transacciones[i], padx=10, pady=100, command=lambda: funcBotones(i)).pack())
    FF = FieldFrame(frameProcesos, "", ["Escoga el usuario cuyas peticiones desea ver"], "", paso2, [[c.getNombre() for c in Banco.getClientes()]])
    FF.pack()
    frameP.forget()


BsolicitarTarjeta = Button(frameP, text="Solicitar tarjeta", command=lambda: procesoSolicitarTarjeta(), padx= 10, pady=10)
BpagarFactura = Button(frameP, text="Pagar factura", command=lambda: procesoPagarFactura(), padx=10, pady=10)
BhacerTransaccion = Button(frameP, text="Hacer transaccion", command=lambda: procesoHacerTransaccion(), padx=10, pady=10)
BdeshacerTransaccion = Button(frameP, text="Deshacer transaccion", command=lambda:procesoDeshacerTransaccion(), padx=10, pady=10)
BverPeticiones = Button(frameP, text="Ver peticiones", command=lambda:procesoVerPeticiones(), padx=10, pady=10)

BsolicitarTarjeta.pack()
BpagarFactura.pack()
BhacerTransaccion.pack()
BdeshacerTransaccion.pack()
BverPeticiones.pack()

notebook.add(frameArchivo, text = "Archivo")
notebook.add(frameProcesos, text = "Procesos y Consultas")
notebook.add(frameAyuda, text = "Ayuda")



root.mainloop()