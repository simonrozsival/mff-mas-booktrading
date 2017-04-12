# Nápady - co by šlo naimplementovat

## `HistorieObchodovaní`

Uchovávala by historii kdo-komu-chtěl-za-kolik-prodat-kterou-knihu-a-zda-to-proběhlo. Po každém obchodu (já někomu chtěl prodat/prodal knihu, někdo ode mne chtěl koupit knihu a mou nabídku ceny přijal/odmítl) se uloží do historie informace o tom, jak obchod proběhl. Na základě těchto informací se mohou upravovat ceny, za které budeme ochotní nakupovat knihy od ostatních a za které budeme ochotní naše knihy kupovat od ostatních.

Lze zaznamenávat i historii poptávek (agent X chtěl koupit knihu Y) a i dle toho ceny upravovat.

## `KterouProdat(ciziAgent, historieObchodovani)`

Na základě agenta a historie nakupování vybere knihu, kterou bych chtěl danému agentovi nabídnout (např. knihu, za kterou od něj očekávám největší zisk).

## `MamKoupit(ciziAgent, kniha, nabizenaCena, historieObchodovani)`

Cizí agent by nám rád prodal nějakou knihu, kterou poptáváme, za nějakou cenu, jsme ochotní knihu za tuto cenu od něj koupit? (Pokud nabízíme v jednom "kole" jednu knihu více agentům, počkáme si na všechny nabídky a zvažujeme jen tu nejvyšší).

## `MamProdat(ciziAgent, kniha, historieObchodovani)`

Cizí agent chce koupit nějakou knihu - funkce rozhodne, zda mu ji chci prodat a případně za jakou cenu jsem ochoten danou knihu prodat?

Pokud více agentů chce koupit nějakou knihu a přijmou mé nabídky, pak v daném kole vyberu toho agenta, který přijal mou nejvyšší nabídku a tomu knihu prodám.

## Testování cenové hladiny

Pokaždé, když nějaký agent chce koupit knihu, tak nikdy neřeknu ne, i kdybych knihu neměl - pošlu mu nějakou nabídku a zjistím, jestli o ni má zájem. I kdyby měl zájem, tak prodej nakonec zruším. __Je tohle vůbec možné? Není to tak, že když už nabídku pošlu, tak ve chvíli, kdy ji přijme, už se to považuje za vyřízenou věc? V případě, že knihu nemám, tak je to vlastně jedno, protože tu transakci stejně nemůžu dokončit, protože knihu nemám...__

