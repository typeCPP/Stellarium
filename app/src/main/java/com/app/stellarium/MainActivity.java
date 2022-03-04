package com.app.stellarium;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.CompatibilityNamesTable;
import com.app.stellarium.database.tables.CompatibilityZodiacTable;
import com.app.stellarium.database.tables.HoroscopePredictionsByPeriodTable;
import com.app.stellarium.database.tables.HoroscopePredictionsTable;
import com.app.stellarium.database.tables.HoroscopeSignCharacteristicTable;
import com.app.stellarium.database.tables.InformationTable;
import com.app.stellarium.database.tables.MoonCalendarTable;
import com.app.stellarium.database.tables.NumerologyTable;
import com.app.stellarium.database.tables.PythagoreanSquareTable;
import com.app.stellarium.database.tables.TaroCardsTable;
import com.app.stellarium.database.tables.ZodiacSignsTable;
import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BadgeDrawable badgeDrawable;
    private KenBurnsView kbv;
    private int numberOfPrevFragment = 2; //1 - personalAccount, 2 - Home, 3 - Information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentHome()).commit();
        bottomNavigationView.findViewById(R.id.ic_home).performClick();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                badgeDrawable = bottomNavigationView.getBadge(item.getItemId());
                if (badgeDrawable != null) {
                    badgeDrawable.clearNumber();
                }
                Fragment fragment = null;
                switch (item.getItemId()) {

                    case R.id.ic_personalAccount:
                        fragment = new FragmentPersonalAccount();
                        if (numberOfPrevFragment == 0) {
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment > 1) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right)
                                    .replace(R.id.frameLayout, fragment).commit();
                        }
                        numberOfPrevFragment = 1;
                        break;
                    case R.id.ic_home:
                        fragment = new FragmentHome();
                        if (numberOfPrevFragment == 0) {
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment == 1) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment == 3) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right)
                                    .replace(R.id.frameLayout, fragment).commit();
                        }
                        numberOfPrevFragment = 2;
                        break;
                    case R.id.ic_information:
                        fragment = new FragmentListOfInformation();
                        if (numberOfPrevFragment == 0) {
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment < 3) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left)
                                    .replace(R.id.frameLayout, fragment).commit();
                        }
                        numberOfPrevFragment = 3;
                        break;
                }
                return true;
            }
        });
        kbv = findViewById(R.id.image11);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        StellariumTransitionGenerator stellariumTransitionGenerator =
                new StellariumTransitionGenerator(10000, adi);
        kbv.setTransitionGenerator(stellariumTransitionGenerator);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        databaseHelper.onUpgrade(database, 2, 2);
        createFillInformationTable(database);
        createFillHoroscopePredictionsByPeriodTable(database);
        createFillHoroscopePredictionsTable(database);
        createFillHoroscopeSignCharacteristicTable(database);
        createFillPythagoreanSquareTable(database);
        createFillZodiacSignsTable(database);
        createFillCompatibilityZodiacTable(database);
        createFillCompatibilityNamesTable(database);
        createFillTaroCardsTable(database);
        createFillNumerologyTable(database);
        createFillMoonCalendarTable(database);
        hideBottomBar(false);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public static void createFillInformationTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(InformationTable.COLUMN_NAME, "АФФИРМАЦИЯ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Аффирмации это круто, это класс, кто не согласен тот...");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ГОРОСКОП");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Все знают что такое гороскоп.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ТАРО");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Карты, деньги, два ствола.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "СОВМЕСТИМОСТЬ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Милый ты любишь меня?");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ЛУННЫЙ КАЛЕНДАРЬ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Стригу волосы только, когда Луна в стадии жопы.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "НУМЕРОЛОГИЯ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Цифры решают всё.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "КВАДРАТ ПИФАГОРА");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Это не похоже на школьную программу.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ДА-НЕТ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Ну тут уж совсем всё просто.");
        database.insert(InformationTable.TABLE_NAME, null, values);
    }

    public static void createFillHoroscopePredictionsByPeriodTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] names = {"Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы"};
        String[] periods = {"21 марта – 20 апреля", "21 апреля – 21 мая", "22 мая – 21 июня",
                "22 июня – 22 июля", "23 июля – 23 августа", "24 августа – 22 сентября",
                "23 сентября – 22 октября", "23 октября – 22 ноября", "22 ноября – 21 декабря",
                "22 декабря – 20 января", "21 января – 19 февраля", "20 февраля – 20 марта"};
        for (int i = 0; i < 12; i++) {
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_SIGN_NAME, names[i]);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_PERIOD_SIGN, periods[i]);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_CHARACTERISTIC_ID, i);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_TODAY_PREDICTION_ID, 5 * i + 1);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_TOMORROW_PREDICTION_ID, 5 * i + 2);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_WEEK_PREDICTION_ID, 5 * i + 3);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_MONTH_PREDICTION_ID, 5 * i + 4);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_YEAR_PREDICTION_ID, 5 * i + 5);
            database.insert(HoroscopePredictionsByPeriodTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillHoroscopePredictionsTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] love = {"Sex будет.", "Sexa не будет."};
        String[] common = {"Всё будет хорошо.", "Всё будет плохо."};
        String[] health = {"Будете здоровы.", "Будете болеть."};
        String[] business = {"Вас ждет куча бабла.", "Вас ждут убытки."};
        for (int i = 0; i < 60; i++) {
            Random random = new Random();
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_LOVE, love[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_LOVE, love[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_COMMON, common[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_COMMON, common[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_HEALTH, health[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_HEALTH, health[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_BUSINESS, business[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_BUSINESS, business[1]);
            }
            database.insert(HoroscopePredictionsTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillHoroscopeSignCharacteristicTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] description = {"Вы ужасный человек.", "Вы прекрасный человек"};
        String[] character = {"Характер у вас так себе.", "Отличный характер."};
        String[] love = {"Вы будете любимы.", "Вас никто не полюит."};
        String[] career = {"Вы станете президентом.", "Вас станете дворником."};
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            if (random.nextInt(100) < 50) {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_LOVE, love[0]);
            } else {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_LOVE, love[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_DESCRIPTION, description[0]);
            } else {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_DESCRIPTION, description[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_CHARACTER, character[0]);
            } else {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_CHARACTER, character[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_CAREER, career[0]);
            } else {
                values.put(HoroscopeSignCharacteristicTable.COLUMN_CAREER, career[1]);
            }
            database.insert(HoroscopeSignCharacteristicTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillPythagoreanSquareTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] descriptions = {"Ну ты лох конечно в этом плане.", "Всё не очень хорошо.", "Ну на троечку.",
                "Красавчик, гордый среднячок.", "У тебя всё отлично в этом плане.", "Богом одаренный.", "Ну это вообще неописуемый дар, лучше некуда."};
        for (int i = 1; i <= 9; i++) {
            values.put(PythagoreanSquareTable.COLUMN_NUMBER, i);
            values.put(PythagoreanSquareTable.COLUMN_NO_NUMBER, descriptions[0]);
            values.put(PythagoreanSquareTable.COLUMN_ONE_NUMBER, descriptions[1]);
            values.put(PythagoreanSquareTable.COLUMN_TWO_NUMBERS, descriptions[2]);
            values.put(PythagoreanSquareTable.COLUMN_THREE_NUMBERS, descriptions[3]);
            values.put(PythagoreanSquareTable.COLUMN_FOUR_NUMBERS, descriptions[4]);
            values.put(PythagoreanSquareTable.COLUMN_FIVE_NUMBERS, descriptions[5]);
            values.put(PythagoreanSquareTable.COLUMN_SIX_NUMBERS, descriptions[6]);
            database.insert(PythagoreanSquareTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillZodiacSignsTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] signs = {"Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы"};
        for (int i = 0; i < signs.length; i++) {
            values.put(ZodiacSignsTable.COLUMN_NAME, signs[i]);
            values.put(ZodiacSignsTable.COLUMN_SIGN_ID, i + 1);
            database.insert(ZodiacSignsTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillCompatibilityZodiacTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        Random random = new Random();
        for (int i = 1; i <= 12; i++) {
            for (int j = 1; j <= 12; j++) {
                values.put(CompatibilityZodiacTable.COLUMN_FIRST_SIGN_ID, i);
                values.put(CompatibilityZodiacTable.COLUMN_SECOND_SIGN_ID, j);

                values.put(CompatibilityZodiacTable.COLUMN_COMP_LOVE_TEXT, "Любовь морковь будет всегда у вас.");
                values.put(CompatibilityZodiacTable.COLUMN_COMP_SEX_TEXT, "Sex будет всегда у вас.");
                values.put(CompatibilityZodiacTable.COLUMN_COMP_MARRIAGE_TEXT, "В браке всё будет отлично.");
                values.put(CompatibilityZodiacTable.COLUMN_COMP_FRIENDSHIP_TEXT, "Будете лучшими друзьями.");

                values.put(CompatibilityZodiacTable.COLUMN_COMP_LOVE_VALUE, random.nextInt(21) + 80);
                values.put(CompatibilityZodiacTable.COLUMN_COMP_SEX_VALUE, random.nextInt(21) + 80);
                values.put(CompatibilityZodiacTable.COLUMN_COMP_MARRIAGE_VALUE, random.nextInt(21) + 80);
                values.put(CompatibilityZodiacTable.COLUMN_COMP_FRIENDSHIP_VALUE, random.nextInt(21) + 80);
                database.insert(CompatibilityZodiacTable.TABLE_NAME, null, values);
            }
        }
    }

    public static void createFillCompatibilityNamesTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        Random random = new Random();
        for (int i = 1; i <= 32; i++) {
            values.put(CompatibilityNamesTable.COLUMN_HASHED_ID, i);

            values.put(CompatibilityNamesTable.COLUMN_COMP_LOVE_TEXT, "Любовь морковь будет всегда у вас.");
            values.put(CompatibilityNamesTable.COLUMN_COMP_FRIENDSHIP_TEXT, "Дружба - это чудо.");
            values.put(CompatibilityNamesTable.COLUMN_COMP_JOB_TEXT, "На работе будет работа.");

            values.put(CompatibilityNamesTable.COLUMN_COMP_LOVE_VALUE, random.nextInt(21) + 80);
            values.put(CompatibilityNamesTable.COLUMN_COMP_FRIENDSHIP_VALUE, random.nextInt(21) + 80);
            values.put(CompatibilityNamesTable.COLUMN_COMP_JOB_VALUE, random.nextInt(21) + 80);
            database.insert(CompatibilityNamesTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillTaroCardsTable(SQLiteDatabase database) {
        String[] namesOfPictures = {"rwpagepentacles", "rwpagecups", "rwmoon", "rwmagician",
                "rwlovers", "rwknightwands", "rwknightswords", "rwknightpentacles", "rwknightcups",
                "rwkingwands", "rwkingswords", "rwkingpentacles", "rwkingcups", "rwjustice", "rwjudgement",
                "rwhighpriestess", "rwhierophant", "rwhermit", "rwhangedman", "rwfool", "rwempress", "rwemperor",
                "rwdevil", "rwdeath", "rwchariot", "rwacewands", "rwaceswords", "rwacepentacles", "rwacepentacles",
                "rwacecups", "rw10wands", "rw10swords", "rw10pentacles", "rw10cups", "rw09wands", "rw09swords",
                "rw09pentacles", "rw09cups", "rw08wands", "rw08swords", "rw08pentacles", "rw08cups",
                "rw07wands", "rw07swords", "rw07pentacles", "rw07cups", "rw06wands", "rw06swords", "rw06pentacles",
                "rw06cups", "rw05wands", "rw05swords", "rw05pentacles", "rw05cups", "rw04wands", "rw04swords",
                "rw04pentacles", "rw04cups", "rw03wands", "rw03swords", "rw03pentacles", "rw03cups", "rw02wands",
                "rw02swords", "rw02pentacles", "rw02cups", "rwworld", "rwwheelfortune", "rwtower",
                "rwtemperance", "rwsun", "rwstrength", "rwstar", "rwqueenwands", "rwqueenswords", "rwqueenpentacles", "rwqueencups", "rwpagewands", "rwpageswords"};
        ContentValues values = new ContentValues();
        for (int i = 0; i < 78; i++) {
            values.put(TaroCardsTable.COLUMN_ID, i);
            values.put(TaroCardsTable.COLUMN_NAME, "Карта");
            values.put(TaroCardsTable.COLUMN_PICTURE_NAME, namesOfPictures[i]);
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_ONE_CARD, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_DAY_CARD, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_FIRST_OF_THREE_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_SECOND_OF_THREE_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_THIRD_OF_THREE_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_FIRST_OF_FOUR_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_SECOND_OF_FOUR_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_THIRD_OF_FOUR_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_FOURTH_OF_FOUR_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_FIRST_OF_SEVEN_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_SECOND_OF_SEVEN_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_THIRD_OF_SEVEN_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_FOURTH_OF_SEVEN_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_FIFTH_OF_SEVEN_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_SIXTH_OF_SEVEN_CARDS, "Описание");
            values.put(TaroCardsTable.COLUMN_DESCRIPTION_SEVENTH_OF_SEVEN_CARDS, "Описание");
            database.insert(TaroCardsTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillNumerologyTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        for (int i = 1; i <= 9; i++) {
            values.put(NumerologyTable.COLUMN_NUMBER, i);
            values.put(NumerologyTable.COLUMN_GENERAL, "общее описание");
            values.put(NumerologyTable.COLUMN_DIGNITIES, "плюсы");
            values.put(NumerologyTable.COLUMN_DISADVANTAGES, "минусы");
            values.put(NumerologyTable.COLUMN_FATE, "судьба");
            database.insert(NumerologyTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillMoonCalendarTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int countOfDaysFebruary = 28;
        if (year % 4 == 0) {
            countOfDaysFebruary = 29;
        }
        for (int j = 1; j <= 12; j++) {
            if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
                for (int i = j * 100 + 1; i <= j * 100 + 31; i++) {
                    values.put(MoonCalendarTable.COLUMN_DATE, i);
                    values.put(MoonCalendarTable.COLUMN_PHASE, "Вот вам яркий пример современных тенденций - постоянный количественный рост и сфера нашей активности говорит о возможностях новых предложений. Приятно, граждане, наблюдать, как многие известные личности будут объявлены нарушающими общечеловеческие нормы этики и морали. Высокий уровень вовлечения представителей целевой аудитории является четким доказательством простого факта: постоянное информационно-пропагандистское обеспечение нашей деятельности позволяет оценить значение первоочередных требований. Имеется спорная точка зрения, гласящая примерно следующее: непосредственные участники технического прогресса и по сей день остаются уделом либералов, которые жаждут быть объединены в целые кластеры себе подобных. Ясность нашей позиции очевидна: выбранный нами инновационный путь говорит о возможностях модели развития.");
                    values.put(MoonCalendarTable.COLUMN_CHARACTERISTIC, "Плохой день");
                    values.put(MoonCalendarTable.COLUMN_HEALTH, "Будете здоровы");
                    values.put(MoonCalendarTable.COLUMN_RELATIONS, "Любви не существует");
                    values.put(MoonCalendarTable.COLUMN_BUSINESS, "Деньги есть");
                    database.insert(MoonCalendarTable.TABLE_NAME, null, values);
                }
            } else if (j == 4 || j == 6 || j == 9 || j == 11) {
                for (int i = j * 100 + 1; i <= j * 100 + 30; i++) {
                    values.put(MoonCalendarTable.COLUMN_DATE, i);
                    values.put(MoonCalendarTable.COLUMN_PHASE, "В этом месяце 30 дней. Вот вам яркий пример современных тенденций - постоянный количественный рост и сфера нашей активности говорит о возможностях новых предложений. Приятно, граждане, наблюдать, как многие известные личности будут объявлены нарушающими общечеловеческие нормы этики и морали. Высокий уровень вовлечения представителей целевой аудитории является четким доказательством простого факта: постоянное информационно-пропагандистское обеспечение нашей деятельности позволяет оценить значение первоочередных требований. Имеется спорная точка зрения, гласящая примерно следующее: непосредственные участники технического прогресса и по сей день остаются уделом либералов, которые жаждут быть объединены в целые кластеры себе подобных. Ясность нашей позиции очевидна: выбранный нами инновационный путь говорит о возможностях модели развития.");
                    values.put(MoonCalendarTable.COLUMN_CHARACTERISTIC, "Хороший день");
                    values.put(MoonCalendarTable.COLUMN_HEALTH, "Будете почти здоровы");
                    values.put(MoonCalendarTable.COLUMN_RELATIONS, "Вы наполнены любовью");
                    values.put(MoonCalendarTable.COLUMN_BUSINESS, "Денег нет, но вы держитесь");
                    database.insert(MoonCalendarTable.TABLE_NAME, null, values);
                }
            } else {
                for (int i = j * 100 + 1; i <= j * 100 + countOfDaysFebruary; i++) {
                    values.put(MoonCalendarTable.COLUMN_DATE, i);
                    values.put(MoonCalendarTable.COLUMN_PHASE, "Ну это февраль. Вот вам яркий пример современных тенденций - постоянный количественный рост и сфера нашей активности говорит о возможностях новых предложений. Приятно, граждане, наблюдать, как многие известные личности будут объявлены нарушающими общечеловеческие нормы этики и морали. Высокий уровень вовлечения представителей целевой аудитории является четким доказательством простого факта: постоянное информационно-пропагандистское обеспечение нашей деятельности позволяет оценить значение первоочередных требований. Имеется спорная точка зрения, гласящая примерно следующее: непосредственные участники технического прогресса и по сей день остаются уделом либералов, которые жаждут быть объединены в целые кластеры себе подобных. Ясность нашей позиции очевидна: выбранный нами инновационный путь говорит о возможностях модели развития.");
                    values.put(MoonCalendarTable.COLUMN_CHARACTERISTIC, "Средний день");
                    values.put(MoonCalendarTable.COLUMN_HEALTH, "Не будете здоровы");
                    values.put(MoonCalendarTable.COLUMN_RELATIONS, "Любовь - страшная сила");
                    values.put(MoonCalendarTable.COLUMN_BUSINESS, "Бизнес прогорел");
                    database.insert(MoonCalendarTable.TABLE_NAME, null, values);
                }
            }
        }
    }

    public void hideBottomBar(boolean isHidden) {
        bottomNavigationView.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    public void setNumberOfPrevFragment() {
        numberOfPrevFragment = 0;
    }
}