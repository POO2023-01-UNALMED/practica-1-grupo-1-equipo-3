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

def setup():
    cliente1 = Cliente("Carlos", 555)
    cliente2 = Cliente("Carolina", 777)
    TarjetaDeb1 = TarjetaDebito(1, Divisa.EURO, 100000)
    TarjetaDeb2 = TarjetaDebito(2, Divisa.DOLAR, 100000)
    TarjetaDeb3 = TarjetaDebito(3, Divisa.DOLAR, 100000)

    TarjetaCred1 = TarjetaCredito(4, Divisa.DOLAR, 10000, 10)

    tarjetaFac = TarjetaDebito(666, Divisa.DOLAR, 100)
    factura1 = Factura(cliente1, 100, 5, tarjetaFac)

    cliente1.agregarTarjetasDebito(TarjetaDeb1, TarjetaDeb2)
    cliente1.agregarTarjetasCredito(TarjetaCred1)

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
    


def procesoPagarFactura():
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
            print(transaccon)
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
    

BsolicitarTarjeta = Button(frameP, text="Solicitar tarjeta", command=lambda: procesoSolicitarTarjeta(), padx= 10, pady=10)
BpagarFactura = Button(frameP, text="Pagar factura", command=lambda: procesoPagarFactura(), padx=10, pady=10)

BsolicitarTarjeta.pack()
BpagarFactura.pack()

notebook.add(frameArchivo, text = "Archivo")
notebook.add(frameProcesos, text = "Procesos y Consultas")
notebook.add(frameAyuda, text = "Ayuda")



root.mainloop()