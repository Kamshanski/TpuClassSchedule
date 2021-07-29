package edu.kamshanski.tpuclassschedule.test_files

import edu.kamshanski.tpuclassschedule.model.loader.responses.SourceResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.OtherInfo
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo
import edu.kamshanski.tpuclassschedule.test_utils.TestWeekScheduleResponse
import edu.kamshanski.tpuclassschedule.test_utils.TestWeekScheduleResponse.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

// TODO: для корректного и красивого отобраения OtherInfo сделать Formatter, который будет смотреть
//  все варианты возмоные на сайте и дизайнить их соответственно. Т.е. для "Выпускающее
//  подразделение: ОЭЭ" нужно выделить "ОЭЭ" жирным и поставить именно на "ОЭЭ" ссылку.
object RaspFilesDb {

    val raspFiles = ArrayList<RaspTestFile>().also {
        var sr: SourceResponse

        it += RaspTestFile( "gruppa_35470",2020, 5, "5А95 (ИШЭ - 2)",
            SourceResponse (
                link = "gruppa_35470",
                info =  ParticipantInfo(
                    fullName = "5А95",
                    nameAppendix = "ИШЭ - 2", // Без скобок
                    otherInfo = listOf(
                        OtherInfo("13.03.02 Электроэнергетика и электротехника", "https://up.tpu.ru/view/detali.html?id=22832"),
                        OtherInfo("Бакалавр (Очная , 2019)", ""),
                        OtherInfo("Выпускающее подразделение: ОЭЭ", "https://tpu.ru/university/structure/department/view?id=8022")
                    ))),
            TestWeekScheduleResponse(
                updateTime = "30.06.2021 11:03:00",
                year = 2020,
                sundayDate = "04.10.20",
                sourceInfo = TestParticipantInfo(
                    fullName = "5А95",
                    nameAppendix = "ИШЭ - 2",
                    otherInfo = mapOf(
                        "13.03.02 Электроэнергетика и электротехника" to "https://up.tpu.ru/view/detali.html?id=22832",
                        "Бакалавр (Очная , 2019)" to "",
                        "Выпускающее подразделение: ОЭЭ" to "https://tpu.ru/university/structure/department/view?id=8022",
                    )
                ),
                scheduleMeta = mapOf(
                    "Учебный процесс: Теоретическое обучение и рассредоточенные практики" to "",
                    "Изменение расписания: 02.11.2020" to "",
                    "Сессия: 11.01.2021" to "",
                ),
                testWeekSchedule = TestWeekSchedule(
                    week = 5,
                    startDate = "28.09.20",
                    testDays = mutableListOf(
                        TestDay(date = "28.09.20",
                            dayOfWeek = "понедельник",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "БЖД",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Сечин А. И."),
                                                    building = "8", room = "316",
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Современ.технологии",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Кривова Л. В.", "Киселев А. В."),
                                                    building = "8", room = "328",
                                                ))))),
                                TestWindow(ordinal = 3,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Механика 1",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Коноваленко И. С."),
                                                    building = "8", room = "330",
                                                ))))),
                                TestWindow(ordinal = 4,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Элект.дисц.по ФКиС",
                                            type = "ПР",
                                        ))),
                            )),
                        TestDay(date = "29.09.20",
                            dayOfWeek = "вторник",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "БЖД",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Сечин А. И."),
                                                    building = "8", room = "201",
                                                ))))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Буран А. Л."),
                                                    building = "8", room = "333",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Пташкин А. С."),
                                                    building = "8", room = "334",
                                                ),
                                            )))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Чеснокова И. А."),
                                                    building = "8", room = "334",
                                                ))))),
                                TestWindow(ordinal = 3,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 3",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Елизарова М. А."),
                                                    building = "8", room = "330",
                                                ))))),
                            )),
                        TestDay(date = "30.09.20",
                            dayOfWeek = "среда",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Физика 2",
                                            type = "ЛБ",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Березнеева Е. В."),
                                                    building = "3", room = " 104",
                                                ))))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Физика 2",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Борисенко С. И."),
                                                    building = "3", room = "215",
                                                ),
                                            )))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 3",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Елизарова М. А."),
                                                    building = "8", room = "101",
                                                ))))),
                                TestWindow(ordinal = 3,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Чеснокова И. А."),
                                                    building = "8", room = "335",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Буран А. Л."),
                                                    building = "8", room = "333",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Пташкин А. С."),
                                                    building = "8", room = "337",
                                                ),
                                            )))),
                            )),
                        TestDay(date = "01.10.20",
                            dayOfWeek = "четверг",
                            testWindows = listOf(
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "М/ведение и ТКМ",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Ковалевская Ж. Г."),
                                                    building = "8", room = " 201",
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 3",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Елизарова М. А."),
                                                    building = " 8", room = "330",
                                                ),
                                            )))),
                                TestWindow(ordinal = 4,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Элект.дисц.по ФКиС",
                                            type = "ПР",
                                        ))),
                            )),
                        TestDay(date = "02.10.20",
                            dayOfWeek = "пятница",
                            testWindows = listOf(
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Механика 1",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Коноваленко И. С."),
                                                    building = "8", room = " 301",
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Физика 2",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Борисенко С. И."),
                                                    building = " 3", room = "210",
                                                ),
                                            )))),
                                TestWindow(ordinal = 3,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Физика 2",
                                            type = "ЛБ",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Борисенко С. И."),
                                                    building = "3", room = "ЭЛ",
                                                ))))),
                            )),
                        TestDay(date = "03.10.20",
                            dayOfWeek = "суббота",
                        )
                    )
                ),
                storedGroups = ContextDb.groups,
                storedActivities = ContextDb.activities,
                storedTeachers = ContextDb.teachers,
                storedPlaces = ContextDb.places,
            ).build())


        it += RaspTestFile("pomeschenie_44", 2019, 7, "Главный учебный корпус - 310 (Поточная лекционная аудитория)",
            SourceResponse (
                link = "pomeschenie_44",
                info =  ParticipantInfo(
                    fullName = "Главный учебный корпус - 310",
                    nameAppendix = "Поточная лекционная аудитория",
                    otherInfo = listOf(
                        OtherInfo("&nbsp;&nbsp;ВКС в режиме конференции", ""),
                        OtherInfo("Поточная лекционная аудитория", ""),
                        OtherInfo("Площадь: 140.8 м2", ""),
                        OtherInfo("Мест: 112", "")
                    ))),
            TestWeekScheduleResponse(
                updateTime = "30.06.2021 11:03:00",
                year = 2019,
                sundayDate = "20.10.19",
                sourceInfo = TestParticipantInfo(
                    fullName = "Главный учебный корпус - 310",
                    nameAppendix = "Поточная лекционная аудитория",
                    otherInfo = mapOf(
                        "&nbsp;&nbsp;ВКС в режиме конференции" to "",
                        "Поточная лекционная аудитория" to "",
                        "Площадь: 140.8 м2" to "",
                        "Мест: 112" to "",
                    )
                ),
                scheduleMeta = mapOf(),
                testWeekSchedule = TestWeekSchedule(
                    week = 7,
                    startDate = "14.10.19",
                    testDays = mutableListOf(
                        TestDay(date = "14.10.19",
                            dayOfWeek = "понедельник",
                            testWindows = listOf(
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 1",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    groups = listOf("4А91", "4Б91", "4В91","4Д91"),
                                                    teachers = listOf("Харлова А. Н."),
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Экономика",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    groups = listOf("8В81", "8В82", "8И81", "8И82", "8К81", "8К82"),
                                                    teachers = listOf("Кащук И. В."),
                                                ))))),
                            )),
                        TestDay(date = "15.10.19",
                            dayOfWeek = "вторник",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Информатика ",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Рыбалка С. А."),
                                                    groups = listOf("1А91", "1А92", "1Д91", "5091"),
                                                ))))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 1",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Шерстнева А. И."),
                                                    groups = listOf("3Н91", "8К91", "8К92", "8К93"),
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Механика 1",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Соколов А. П."),
                                                    groups = listOf("4Т81", "5А85", "5А86", "5А87"),
                                                ))))),
                                TestWindow(ordinal = 4,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Заседание по программе \" Скорая образовательная помощь\"",
                                            type = "ЛК"
                                        ))),
                            )),
                        TestDay(date = "16.10.19",
                            dayOfWeek = "среда",
                            testWindows = listOf(
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Наноэлектроника",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Арышева Г. В."),
                                                    groups = listOf("1А7А", "1А7Б")
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИБП тхн комп,сист. ",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Глазачев А. В."),
                                                    groups = listOf("5АМ88"),
                                                ),
                                            )))),
                            )),
                        TestDay(date = "17.10.19",
                            dayOfWeek = "четверг",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 3",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Шерстнева А. И."),
                                                    groups = listOf("1Б81", "1Б82", "1Е81", "8К81", "8К82"),
                                                ))))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Электроника 2.2",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Гребенников В. В."),
                                                    groups = listOf("1Б7А", "1Б7Б", "1Д71", "4В71"),
                                                    ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Информатика",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Воронина Н. М."),
                                                    groups = listOf("2Г91", "2У91", "8В91", "8В92", "8Д91"),
                                                    ))))),
                            )),
                        TestDay(date = "18.10.19",
                            dayOfWeek = "пятница",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Математика 1",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Шерстнева А. И."),
                                                    groups = listOf("3Н91", "8К91", "8К92", "8К93")
                                                ))))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Совр.тхи энергетики",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Матвеева А. А."),
                                                    groups = listOf("5Б81", "5Б82", "5Б83")
                                                ),
                                            )))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИБП тхн комп,сист.",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Глазачев А. В."),
                                                    groups = listOf("5АМ88")
                                                ))))),
                            )),
                        TestDay(date = "19.10.19",
                            dayOfWeek = "суббота",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Эл/н.пром.устр.",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Солдатов А. И."),
                                                    groups = listOf("1АМ91", "1АМ92")
                                                ))))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Эл/н,эл/мех.устр-ва",
                                            type = "ЛК",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Мартемьянов В. М."),
                                                    groups = listOf("1А81", "1А82")
                                                ),
                                            )))),
                            )),
                    )
                ),
                storedGroups = ContextDb.groups,
                storedActivities = ContextDb.activities,
                storedTeachers = ContextDb.teachers,
                storedPlaces = ContextDb.places,
            ).build()
        )

        it += RaspTestFile("user_61007", 2020, 6, "Смирнова Ульяна Александровна",
            SourceResponse (
                link = "user_61007",
                info =  ParticipantInfo(
                    fullName = "Смирнова Ульяна Александровна",
                    nameAppendix = "",
                    otherInfo = listOf(
                        OtherInfo("+73822606230", ""),
                        OtherInfo("+73822701777,2256#", ""),
                        OtherInfo("ulsmirnova@tpu.ru", ""),
                    ))),
            TestWeekScheduleResponse(
                updateTime = "30.06.2021 11:03:00",
                year = 2020,
                sundayDate = "11.10.20",
                sourceInfo = TestParticipantInfo(
                    fullName = "Смирнова Ульяна Александровна",
                    otherInfo = mapOf(
                        "+73822606230" to "",
                        "+73822701777,2256#" to "",
                        "ulsmirnova@tpu.ru" to "",
                    )
                ),
                scheduleMeta = mapOf(),
                testWeekSchedule = TestWeekSchedule(
                    week = 6,
                    startDate = "05.10.20",
                    testDays = mutableListOf(
                        TestDay(date = "05.10.20",
                            dayOfWeek = "понедельник",
                            testWindows = listOf(
                                TestWindow(ordinal = 4,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    groups = listOf("0А95", "0Б91"),
                                                    building = "10", room = "222",
                                                ),
                                            )))),
                            )
                        ),
                        TestDay(date = "06.10.20",
                            dayOfWeek = "вторник",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    groups = listOf("8В01", "8В03", "8Д01", "8Е01", "8Е02", "8К01", "8К02", "8К03"),
                                                    building = "10", room = "223",
                                                ),
                                            )))),
                            )),
                        TestDay(date = "07.10.20",
                            dayOfWeek = "среда"),
                        TestDay(date = "08.10.20",
                            dayOfWeek = "четверг"),
                        TestDay(date = "09.10.20",
                            dayOfWeek = "пятница",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    groups = listOf("8В01", "8В03", "8Д01", "8Е01", "8Е02", "8К01", "8К02", "8К03"),
                                                    building = "10", room = "201/2",
                                                ),
                                            )))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    groups = listOf("0А95", "0Б91"),
                                                    building = "10", room = "223",
                                                ),
                                            )))),
                            )),
                        TestDay(date = "10.10.20",
                            dayOfWeek = "суббота",
                        )
                    )
                ),
                storedGroups = ContextDb.groups,
                storedActivities = ContextDb.activities,
                storedTeachers = ContextDb.teachers,
                storedPlaces = ContextDb.places,
            ).build()
        )

        it += RaspTestFile( "gruppa_33012", 2020, 6, "7ПК190 (УОД - Архив)",
            SourceResponse (
                link = "gruppa_33012",
                info =  ParticipantInfo(
                    fullName = "7ПК190",
                    nameAppendix = "УОД - Архив",
                    otherInfo = listOf(
                        OtherInfo("( , 2017)", ""),
                        OtherInfo("Выпускающее подразделение:", "")
                    ))),
            TestWeekScheduleResponse(
                updateTime = "30.06.2021 11:03:00",
                year = 2020,
                sundayDate = "11.10.20",
                sourceInfo = TestParticipantInfo(
                    fullName = "7ПК190",
                    nameAppendix = "УОД - Архив",
                    otherInfo = mapOf(
                        "( , 2017)" to "",
                        "Выпускающее подразделение:" to "",
                    )),
                scheduleMeta = mapOf(),
                testWeekSchedule = TestWeekSchedule(
                    week = 6,
                    startDate = "05.10.20",
                    testDays = mutableListOf(
                        TestDay(date = "05.10.20",
                            dayOfWeek = "понедельник"
                        ),
                        TestDay(date = "06.10.20",
                            dayOfWeek = "вторник",),
                        TestDay(date = "07.10.20",
                            dayOfWeek = "среда",),
                        TestDay(date = "08.10.20",
                            dayOfWeek = "четверг"),
                        TestDay(date = "09.10.20",
                            dayOfWeek = "пятница",),
                        TestDay(date = "10.10.20",
                            dayOfWeek = "суббота",
                        )
                    )
                ),
                storedGroups = ContextDb.groups,
                storedActivities = ContextDb.activities,
                storedTeachers = ContextDb.teachers,
                storedPlaces = ContextDb.places,
            ).build()
        )

        it += RaspTestFile( "gruppa_37020", 2020, 1, "1А01 (ИШНКБ - 1)",
            SourceResponse (
                link = "gruppa_37020",
                info =  ParticipantInfo(
                    fullName = "1А01",
                    nameAppendix = "ИШНКБ - 1",
                    otherInfo = listOf(
                        OtherInfo("11.03.04 Электроника и наноэлектроника", "https://up.tpu.ru/view/detali.html?id=24573"),
                        OtherInfo("Бакалавр (Очная , 2020)", ""),
                        OtherInfo("Выпускающее подразделение: ОЭИ", "https://tpu.ru/university/structure/department/view?id=7977"),
                    ))),

            TestWeekScheduleResponse(
                updateTime = "30.06.2021 11:03:00",
                year = 2020,
                sundayDate = "06.09.20",
                sourceInfo = TestParticipantInfo(
                    fullName = "1А01",
                    nameAppendix = "ИШНКБ - 1",
                    otherInfo = mapOf(
                        "11.03.04 Электроника и наноэлектроника" to "https://up.tpu.ru/view/detali.html?id=24573",
                        "Бакалавр (Очная , 2020)" to "",
                        "Выпускающее подразделение: ОЭИ" to "https://tpu.ru/university/structure/department/view?id=7977",
                    )
                ),
                scheduleMeta = mapOf(
                    "Учебный процесс: Теоретическое обучение и рассредоточенные практики" to "",
                    "Изменение расписания: 02.11.2020" to "",
                    "Сессия: 11.01.2021" to "",
                ),
                testWeekSchedule = TestWeekSchedule(
                    week = 1,
                    startDate = "31.08.20",
                    testDays = mutableListOf(
                        TestDay(date = "31.08.20",
                            dayOfWeek = "понедельник"
                        ),
                        TestDay(date = "01.09.20",
                            dayOfWeek = "вторник",
                            testWindows = listOf(
                                TestWindow(ordinal = 0,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Встречи и события",
                                            link = "https://rasp.tpu.ru/event/view.html?id=823",
                                            type = "ОС",
                                            testContext = listOf(
                                                TestContext(
                                                    building = "ГК", room = "209",
                                                ),
                                            )))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Час куратора",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Иванова В. С."),
                                                    building = "19", room = "141",
                                                ),
                                            )))),
                                TestWindow(ordinal = 4,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Организационное собрание по порядку проведения тестирования",
                                            link = "https://rasp.tpu.ru/event/view.html?id=824",
                                            type = "ОС",))),
                            )),
                        TestDay(date = "02.09.20",
                            dayOfWeek = "среда",
                            testWindows = listOf(
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Час куратора",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Иванова В. С."),
                                                    building = "ГК", room = "310",
                                                ),
                                            )))),
                                TestWindow(ordinal = 2,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Входное тестирование для 1 курса бакалавриата и специалитета",
                                            link = "https://rasp.tpu.ru/event/view.html?id=825",
                                            type = "ПР"))),
                            )),
                        TestDay(date = "03.09.20",
                            dayOfWeek = "четверг",
                            testWindows = listOf(
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Час куратора",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Иванова В. С."),
                                                    building = "ГК", room = "209",
                                                ),
                                            )))),
                                TestWindow(ordinal = 3,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "ИЯ (английский)",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Лахотюк Л. А."),
                                                    building = "7", room = "211/3",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Бекишева Т. Г."),
                                                    building = "7", room = "206",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Куимова М. В."),
                                                    building = "10", room = "219",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Цепилова А. В."),
                                                    building = "10", room = "201/2",
                                                ),
                                                TestContext(
                                                    teachers = listOf("Макаровских А. В."),
                                                    building = "20", room = "235",
                                                ),
                                            )))),
                            )),
                        TestDay(date = "04.09.20",
                            dayOfWeek = "пятница",
                            testWindows = listOf(
                                TestWindow(ordinal = 3,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Час куратора",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Иванова В. С."),
                                                    building = "ГК", room = "310",
                                                ),
                                            )))),
                                TestWindow(ordinal = 1,
                                    testActivities = listOf(
                                        TestActivity(
                                            name = "Знач.пс/лог.освед.",
                                            type = "ПР",
                                            testContext = listOf(
                                                TestContext(
                                                    teachers = listOf("Кудрявцева О. В."),
                                                    building = "19", room = "142",
                                                ),
                                            )))),
                            )),
                        TestDay(date = "05.09.20",
                            dayOfWeek = "суббота",
                        )
                    )
                ),
                storedGroups = ContextDb.groups,
                storedActivities = ContextDb.activities,
                storedTeachers = ContextDb.teachers,
                storedPlaces = ContextDb.places,
            ).build()
        )

        it += RaspTestFile( "user_60566", 2019, 7, "Рыбалка Сергей Анатольевич",
            SourceResponse (
                link = "user_60566",
                info =  ParticipantInfo(
                    fullName = "Рыбалка Сергей Анатольевич",
                    nameAppendix = "",
                    otherInfo = listOf(
                        OtherInfo("Кандидат технических наук", ""),
                        OtherInfo("доцент (ОМИ, ШБИП)", ""),
                        OtherInfo("+73822701777,1167#", ""),
                        OtherInfo("rybalka@tpu.ru", ""),
                    ))))

        it += RaspTestFile( "gruppa_37032", 2020, 6, "8В03 (ИШИТР - 1)",
            SourceResponse (
                link = "gruppa_37032",
                info =  ParticipantInfo(
                    fullName = "8В03",
                    nameAppendix = "ИШИТР - 1",
                    otherInfo = listOf(
                        OtherInfo("09.03.01 Информатика и вычислительная техника", "https://up.tpu.ru/view/detali.html?id=24610"),
                        OtherInfo("Бакалавр (Очная , 2020)", ""),
                        OtherInfo("Выпускающее подразделение: ОИТ", "https://tpu.ru/university/structure/department/view?id=7951"),
                    ))))

        it += RaspTestFile( "sooruzhenie_11", 2020, 6, "Учебный корпус № 10",
            SourceResponse (
                link = "sooruzhenie_11",
                info =  ParticipantInfo(
                    fullName = "Учебный корпус № 10",
                    nameAppendix = "",
                    otherInfo = listOf(
                        OtherInfo("Учебно-научное", ""),
                        OtherInfo("Адрес: г. Томск, пр-кт. Ленина (634028), д.2", ""),
                        OtherInfo("Аудиторий: 130", ""),
                    ))))
    }


    fun loadAsResponseBody(file: RaspTestFile) : ResponseBody {
        val payload = loadRaspFile(file)
        val responseBody = payload.toResponseBody("application/html".toMediaType())
        return responseBody
    }
}
