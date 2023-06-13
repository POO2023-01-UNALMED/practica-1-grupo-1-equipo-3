import tkinter as tk
from tkinter.ttk import Notebook
from tkinter import ttk, messagebox, Frame, Button, Label
from FieldFrame import FieldFrame


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

def aceptar():
    messagebox.showinfo(message="Mensaje", title="Título")


FF = FieldFrame(frameProcesos, "Titulo", ["Crit1", "Crit2", "Crit3"], "TituloValores", aceptar, [None, None, ["Op1", "Op2", "Op3"]])
FF.pack()


notebook.add(frameArchivo, text = "Archivo")
notebook.add(frameProcesos, text = "Procesos y Consultas")
notebook.add(frameAyuda, text = "Ayuda")



root.mainloop()