package edu.kamshanski.tpuclassschedule.model.rasp_entities.constants

/**
 * Activity type
// Бланочное тестирование
// Вебинар
// Государственная экзаменационная комиссия
// Государственный экзамен
// День открытых дверей
// Дифференцированный зачёт // https://www.proz.com/kudoz/russian-to-english/education-pedagogy/853348-дифференцированный-зачет.html
// Зачёт
// Компьютерное тестирование
// Конкурс
// Консультация
// Конференц-неделя
// Конференция
// Концерт
// Курсовая работа
// Курсовой проект
// Лабораторная работа
// Лекция
// Мастер-класс
// Научная школа
// Научное шоу
// Олимпиада
// Организационное собрание
// Практика
// Презентация
// Рабочее совещание
// Самостоятельная работа
// Семинар
// Устное собеседование
// Фильм
// Экзамен
// Экскурсия
 * @constructor Create empty Activity type
 */

// Очень много объектов и используются только некоторые - Лучше заменить на стринги или инты
enum class ActivityType(val acronym: String) {

    FORM_TEST("БТ"),
    WEBINAR("ВБ"),
    STATE_EXAMINATION_COMMISSION("ГЭК"),
    STATE_EXAM("ГЭ"),
    OPEN_DAY("ДОД"),
    GRADED_TEST("ДФ"),   // https://www.proz.com/kudoz/russian-to-english/education-pedagogy/853348-дифференцированный-зачет.html
    CREDIT("ЗЧ"),
    COMPUTER_TEST("КТ"),
    COMPETITION("КО"),
    TUTORIAL("КС"),
    CONFERENCE_WEEK("КН"),
    CONFERENCE("КФ"),
    CONCERT("К"),
    TERM_PAPER("КР"),
    TERM_PROJECT("КП"),
    LABORATORY("ЛБ"),
    LECTURE("ЛК"),
    MASTER_CLASS("МК"),
    SCIENTIFIC_SCHOOL("НШ"),
    SCIENTIFIC_SHOW("НУ"),
    OLYMPIAD("ОЛ"),
    PLANNING_MEETING("ОС"),
    PRACTICE("ПР"),
    PRESENTATION("ПРез"),
    WORKSHOP("РС"),
    INDEPENDENT_WORK("СР"), // хз
    SEMINAR("СМ"),
    INTERVIEW("УС"),
    FILM("Ф"),
    EXAM("Э"),
    EXCURSION("ЭК");

    companion object {
        @JvmStatic fun getByAcronym(acronym: String) : ActivityType? {
            return values().find { it.acronym == acronym }
        }
    }

}
