import tkinter as tk
from tkinter.ttk import Notebook
from tkinter import ttk, messagebox, Frame, Button, Label
from FieldFrame import FieldFrame
from Banco import Banco
from Cliente import Cliente
from Divisa import Divisa

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
    def segundoPaso():
        if FF.getValores()[0] == "" or FF.getValores()[1] == "":
            print("Error cogido")
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