import tkinter as tk
from tkinter.ttk import Notebook
from tkinter import ttk, messagebox, Frame, Button, Label
from FieldFrame import FieldFrame
from Banco import Banco
from Cliente import Cliente
from Divisa import Divisa
from Tarjeta import Tarjeta
from TarjetaCredito import TarjetaCredito
from Factura import Factura

def setup():
    cliente1 = Cliente("Carlos", 555)
    cliente2 = Cliente("Carolina", 777)

setup()

root = tk.Tk()
root.geometry("500x300")

root.title("Banco Nacho")

notebook = Notebook(root)
notebook.pack(pady = 10, expand=True)



frameArchivo = Frame(notebook, width=400, height=280)
frameProcesos = Frame(notebook, width=400, height=280)
frameAyuda = Frame(notebook, width=400, height=280)

frameArchivo.pack(fill="both", expand=True)
frameProcesos.pack(fill="both", expand=True)
frameAyuda.pack(fill="both", expand=True)


frameP = Frame(frameProcesos, background="red",)
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
    


BsolicitarTarjeta = Button(frameP, text="Solicitar tarjeta", command=lambda: procesoSolicitarTarjeta())
BsolicitarTarjeta.pack()

notebook.add(frameArchivo, text = "Archivo")
notebook.add(frameProcesos, text = "Procesos y Consultas")
notebook.add(frameAyuda, text = "Ayuda")



root.mainloop()