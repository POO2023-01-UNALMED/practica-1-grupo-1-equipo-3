from tkinter import *
from tkinter import messagebox

class FieldFrame(Frame):


    def __init__(self, ventana, tituloCriterios, criterios, tituloValores, funcionAceptar = None, opciones = None, valores = None, habilitado = None):
        super().__init__(ventana,  highlightthickness=2, highlightbackground="black")
        tituloC = Label(self, text = tituloCriterios)
        tituloV = Label(self, text = tituloValores)
        tituloC.grid(row = 0, column = 0, padx = 10, pady = 10)
        tituloV.grid(row = 0, column= 1, padx = 10, pady = 10)
        labels = []
        entrys = []
        Vals = [None]*len(criterios)
        for i in range(len(criterios)):
            labels.append(Label(self, text = criterios[i]))
            if not opciones is None:
                if opciones[i] is None:
                    entrys.append(Entry(self))
                else:
                    Vals[i] = StringVar()
                    print(Vals[i])
                    entrys.append(OptionMenu(self, Vals[i], *opciones[i]))
            else:
                entrys.append(Entry(self))
        if not valores is None:
            for i in range(len(valores)):
                entrys[i].insert(0, valores[i])

        for i in range(len(criterios)):
            labels[i].grid(row = i+2, column = 0, padx = 10, pady = 10)
            entrys[i].grid(row = i+2, column = 1, padx = 10, pady = 10)
        
        def borrar():
            for i in range(len(entrys)):
                if isinstance(entrys[i], Entry):
                    entrys[i].delete(0, END)
                else:
                    Vals[i].set("")
        Borrar = Button(self, text="Borrar", command=borrar)
        def funcBotonAceptar(): # Verifica que el usuario halla ingresado datos en todos los campos, y si sí, continúa
            for i in range(len(self.entrys)):
                if isinstance(self.entrys[i], Entry):
                    if self.entrys[i].get() == "":
                        messagebox.showinfo(title="Error", message="Por favor, ingrese valores válidos en todos los campos")
                        return
                else:
                    if self.Vals[i].get() == "":
                        messagebox.showinfo(title="Error", message="Por favor, ingrese valores válidos en todos los campos")
                        return
            funcionAceptar()
        Aceptar = Button(self, text="Aceptar", command=lambda: funcBotonAceptar())
        Borrar.grid(column=0, row = len(criterios)+3, pady=10)
        Aceptar.grid(column=1, row=len(criterios)+3, pady=10)
        self.entrys = entrys
        self.Vals = Vals
    
    def getValores(self):
        retorno = []
        for i in range(len(self.entrys)):
            if isinstance(self.entrys[i], Entry):
                retorno.append(self.entrys[i].get())
            else:
                retorno.append(self.Vals[i].get())
        return retorno
    
