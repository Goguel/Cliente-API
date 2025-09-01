INSERT INTO usuarios(login, senha)
SELECT 'admin', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE login = 'admin');