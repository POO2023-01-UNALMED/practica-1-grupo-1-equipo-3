from tkinter import *
from tkinter import messagebox
from Banco.src.gestorAplicacion.Errores.ErrorOpciones import ErrorOpciones


class FieldFrame(Frame):

    def __init__(self, ventana, tituloCriterios, criterios, tituloValores, funcionAceptar=None, opciones=None,
                 valores=None, habilitado=None):
        """
        Crea una instancia de FieldFrame, que es un marco con campos de entrada y botones asociados.

        Args:
            ventana (Tk): Ventana principal donde se ubicará el FieldFrame.
            tituloCriterios (str): Título para la columna de criterios.
            criterios (List[str]): Lista de criterios.
            tituloValores (str): Título para la columna de valores.
            funcionAceptar (callable): Función a ejecutar cuando se presione el botón "Aceptar".
            opciones (List[List[str]]): Lista de opciones para los campos de selección (OptionMenu).
            valores (List[str]): Valores iniciales para los campos de entrada.
            habilitado (List[bool]): Lista de booleanos para habilitar/deshabilitar los campos de entrada.

        """
        super().__init__(ventana, highlightthickness=2, highlightbackground="black")
        tituloC = Label(self, text=tituloCriterios)
        tituloV = Label(self, text=tituloValores)
        tituloC.grid(row=0, column=0, padx=10, pady=10)
        tituloV.grid(row=0, column=1, padx=10, pady=10)
        labels = []
        entrys = []
        Vals = [None] * len(criterios)
        if opciones is not None:
            for array in opciones:
                if array is not None:
                    if len(array) == 0:
                        raise ErrorOpciones
            
        for i in range(len(criterios)):
            labels.append(Label(self, text=criterios[i]))
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
            labels[i].grid(row=i + 2, column=0, padx=10, pady=10)
            entrys[i].grid(row=i + 2, column=1, padx=10, pady=10)

        def borrar():
            for i in range(len(entrys)):
                if isinstance(entrys[i], Entry):
                    entrys[i].delete(0, END)
                else:
                    Vals[i].set("")

        Borrar = Button(self, text="Borrar", command=borrar)

        def funcBotonAceptar():  # Verifica que el usuario halla ingresado datos en todos los campos, y si sí, continúa
            for i in range(len(self.entrys)):
                if isinstance(self.entrys[i], Entry):
                    if self.entrys[i].get() == "":
                        messagebox.showinfo(title="Error",
                                            message="Por favor, ingrese valores válidos en todos los campos")
                        return
                else:
                    if self.Vals[i].get() == "":
                        messagebox.showinfo(title="Error",
                                            message="Por favor, ingrese valores válidos en todos los campos")
                        return
            funcionAceptar()

        Aceptar = Button(self, text="Aceptar", command=lambda: funcBotonAceptar())
        Borrar.grid(column=0, row=len(criterios) + 3, pady=10)
        Aceptar.grid(column=1, row=len(criterios) + 3, pady=10)
        self.entrys = entrys
        self.Vals = Vals

    def getValores(self):
        """
        Obtiene los valores ingresados en los campos de entrada.

        Returns:
            List[str]: Lista de valores ingresados.

        """
        retorno = []
        for i in range(len(self.entrys)):
            if isinstance(self.entrys[i], Entry):
                retorno.append(self.entrys[i].get())
            else:
                retorno.append(self.Vals[i].get())
        return retorno
