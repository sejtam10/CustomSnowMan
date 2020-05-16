Simulácia banky a bankových účtov - rieš objektovo orientovane (OOP)
======
a. Vytvor plugin: pre klienta banky – ovládanie pomocou chatu => príkazov, vypisovanie informácii
pomocou chatu

- na začiatok nejaké jednoduché vybratie používateľa/prihlásenie, ktorý môže ďalej robiť operácie so svojim účtom. Pri zapnutí teda bude defaultne vytvorených zopár účtov slúžiacich na testovanie.
- Odhlásenie používateľa a znova možnosť prihlásenia

b. Operácie s bankovým účtom (každá operácia musí mať timestamp - kedy sa stala)

- Výber - odčítava od stavu účtu
- Prevod - odčítava od odosielateľa a pripočítava k prijímateľovi, možnosť pridať správu pre prijímateľa, meno prijímateľa a odosielateľa
- Hotovostný vklad - pričítava k stavu účtu
- Poplatok - odčíta od stavu účtu

c. 2 typy bankových účtov - Študentský a normálny

- Na normálnom účte bude za každú operáciu ROZDIELNY poplatok (je na teba v akej hodnote)
- Študentský účet bude bez poplatkov

d. Zaznamenávaj každú operáciu na bankovom účte (históriu)

- pridaj možnosť zobraziť históriu operácií pre prihláseného používateľa

e. Zaznamenávanie každej operácie na každom bankovom účte - bankový log

- Keď nie je prihlásený žiaden používateľ, tak bude možnosť vypísať bankový log
