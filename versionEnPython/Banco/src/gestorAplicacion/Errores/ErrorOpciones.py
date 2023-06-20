from Banco.src.gestorAplicacion.Errores.ErrorAplicacion import ErrorAplicacion

class ErrorOpciones(ErrorAplicacion):
    def __str__(self) -> str:
        return super().__str__() + " Por falta de opciones válidas"
