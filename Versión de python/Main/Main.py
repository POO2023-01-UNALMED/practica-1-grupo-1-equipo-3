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


notebook.add(frameArchivo, text = "Archivo")
notebook.add(frameProcesos, text = "Procesos y Consultas")
notebook.add(frameAyuda, text = "Ayuda")



root.mainloop()