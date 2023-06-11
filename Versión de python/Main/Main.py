import tkinter as tk
from tkinter import ttk, messagebox, Frame
from FieldFrame import FieldFrame

def solTarjetaCredito():
    messagebox.showinfo(message = "examen", title = "examen")


root = tk.Tk()
root.geometry("500x300")

root.title("Banco Nacho")

frame = FieldFrame(root, "Criterios", ["crit1", "crit2"], "valor")
frame.pack()




root.mainloop()