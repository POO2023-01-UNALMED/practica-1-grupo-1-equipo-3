from tkinter import *

class FieldFrame(Frame):


    def __init__(self, ventana, tituloCriterios, criterios, tituloValores, funcionAceptar = None, opciones = None, valores = None, habilitado = None):
        super().__init__(ventana,  highlightthickness=2, highlightbackground="black")
        tituloC = Label(self, text = tituloCriterios)
        tituloV = Label(self, text = tituloValores)
        tituloC.grid(row = 0, column = 0, padx = 10, pady = 10)
        tituloV.grid(row = 0, column= 1, padx = 10, pady = 10)
        labels = []
        entrys = []

        for i in range(len(criterios)):
            labels.append(Label(self, text = criterios[i]))
            Vals = [None]*len(criterios)
            if not opciones is None:
                if opciones[i] is None:
                    entrys.append(Entry(self))
                else:
                    Vals[i] = StringVar()
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
        Aceptar = Button(self, text="Aceptar", command=lambda: funcionAceptar())
        Borrar.grid(column=0, row = len(criterios)+3, pady=10)
        Aceptar.grid(column=1, row=len(criterios)+3, pady=10)
        self.entrys = entrys
        self.Vals = Vals
    
    def getValores(self):
        retorno = []
        for i in range(len(self.entrys)):
            print(self.Vals)
            if isinstance(self.entrys[i], Entry):
                retorno.append(self.entrys[i].get())
            else:
                retorno.append(self.Vals[i].set(""))
        return retorno
    
