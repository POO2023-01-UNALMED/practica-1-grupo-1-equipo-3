from Banco.src.gestorAplicacion.Errores.ErrorAplicacion import ErrorAplicacion

class ErrorInput(ErrorAplicacion):
    def __str__(self) -> str:
        return super().__str__() + " por input ingresado incorrectamente"

