class ErrorAplicacion(Exception):
    def __str__(self) -> str:
        return "Error desde aplicación"
    