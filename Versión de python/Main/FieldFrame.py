from tkinter import Frame, Label, Entry

class FieldFrame(Frame):

    def __init__(self, ventana, tituloCriterios, criterios, tituloValores, valores = None, habilitado = None):
        super().__init__(ventana, width=2000, height = 1000, highlightthickness=2, highlightbackground="black")
        tituloC = Label(self, text = tituloCriterios)
        tituloV = Label(self, text = tituloValores)
        tituloC.grid(row = 0, column = 0, padx = 10, pady = 10)
        tituloV.grid(row = 0, column= 1, padx = 10, pady = 10)
        labels = []
        entrys = []
        for texto in criterios:
            labels.append(Label(self, text = texto))
            entrys.append(Entry(self))
        if not valores is None:
            for i in range(len(valores)):
                entrys[i].insert(0, valores[i])

        for i in range(len(criterios)):
            labels[i].grid(row = i+2, column = 0, padx = 10, pady = 10)
            entrys[i].grid(row = i+2, column = 1, padx = 10, pady = 10)
