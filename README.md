# СПбПУ

## title.tex

Титульный лист для НИР по `ГОСТ 7.32-2001`.

Для начала задайте необходимые переменные:

```latex
\newcommand{\insertInstitute}{
  Институт компьютерных наук и технологий\linebreak
  Высшая школа «Киберфизических систем и управления»
}
\newcommand{\insertTitle}{Название работы}
\newcommand{\insertAuthor}{И. О. Выполняющий}
\newcommand{\insertVerifier}{И. О. Проверящий}
\newcommand{\insertVerifierPosition}{Главный проверяющий}
```

Вставьте лист в начало документа:

```latex
\begin{document}
  \input{../../title.tex}
\end{document}
```

## common.tex

Общие настройки для преамбулы по `ГОСТ 7.32-2001`.

```latex
\input{../../common.tex}
```

## Makefile

* `clear` - удалить все временные файлы после генерации LaTeX.
