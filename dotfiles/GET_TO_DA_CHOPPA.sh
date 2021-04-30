#!/usr/bin/env bash

#------------------------------FUNCTIONS------------------------------
function validateDateArgumentGiven() {
  if [ $# -eq 0 ]; then
    __msg_error "No commit message supplied as argument. Halting script execution."
    __msg_info "Example usage: sh GET_TO_DA_CHOPPA.sh \"Fixing unit test and refactor\""
    exit 1
  fi
}

function printArnold() {
  cat <<"EOF"

                     ______
                   <((((((\\\
                   /      . }\
                   ;--..--._|}
(\                 '--/\--'  )
 \\                | '-'  :'|
  \\               . -==- .-|
   \\               \.__.'   \--._
   [\\          __.--|       //  _/'--.
   \ \\       .'-._ ('-----'/ __/      \
    \ \\     /   __>|      | '--.       |
     \ \\   |   \   |     /    /       /
      \ '\ /     \  |     |  _/       /
       \  \       \ |     | /        /
        \  \      \        /`
EOF
}

function printPredator() {
  cat <<"EOF"

                                 ________
                              .-'XXXXXXXX`._
                             ;XXX        XXX`.
                      -=#######.            XX`.
                       -=###########.         XX:
                        -=#############.       XX:
                           (%(   _#######     #####
                            )/\ .\#\__####. ,###$:;
                   ___    _//%/) .\#(@)\#####(@)/:;
                  //((  _/%/%/(          `###    ;;
                 //  \\//%/%/%/)           ##   :;
        ____    //   ((/%/%/%/(           /TT\  :;
       ( __ \___\\    \\/%/%/%/)      `. /TTTT\ ;;
        \\_\_____\\   ((%/%/%/(         )TTTTTT(;
         \_\_     \\   \\%/%/%/         )TTTTTT(
           \_\_    \\  ((/%/%/          )TTTTTT(
             \_\_   \\  \\/%/        `. )-MEPH-(
            _/X\_\_  \\ ((\(___________/ `----'
          _/X   =\_\//\\ )) ||;=  ;  =;WW.... ==J.
        _/X       .\/ M.--. ||  =;=  ;<MM>   :  ==J.
       /X             ((OO)) \\_;  =;= WW:=     : ==J.
  __.-'                `--'    \\_ ;  <MM>: L    :  =J.
  `=====                         \\   ;WW  L'  :  :  =J.
      _/==                       // =;<MM> L :  :  :  =J.
     /    ==     /  ,========.  //  ; WW : L. :  :  :  ==J.
__.-'           / ,=^        ^=// =;=<MM>=:'L  :  :  :  :=J. /\
`=======       /,=^           //  ;   WW   HHL==   :  :  :=J/ /
    /   ==    /=^   ,=====.  // =;= <MM>: =HH `L==  :  :  :/  \
__.'         (\\  ,=^     ^=//  ;  =;WW =: HH   `L== : _:_/    \
`=====       / \\=^        // =;=  ;<MM>  :HH     `L=_/   .-_-_-\
   ./ ==    (   \\  ,====.//  ;  =;= WW:  =HH    (__      ||     \_
__.'        /    \\=^ ___// =;=  ; <MM>=:= HH       \___  || ......\
`====      (      \\_/___/  ;  =;=  WW   : HH           \_||: .-_-_-\
   | ==    |       )_/ ;  =;=  ;   <MM>  =:HH             \_: || ....\
   |       |       LJ=;=  ;  =;=  ;WW =:=  HH               \_||:     \
   ;       |        LJ  =;=  ;  =;<MM>  :  LH                 \_: .-_-_\
  |        |        HH  ;  =;=  ; WW :  =:= LL                  \ ||    \
  |        |        JJ=;=  ;  =;=<MM>=:=  : HH                   \||     \
  |        |       JJ ;  =;=  ;   WW   :  =:HH                    \    .#M)
  |   __   |      JJ=;=  ;  =;=  <MM>  =:=  HH                     \  /;M(
  |  //\\  |      MX;  =;=  ;  =;=WW :=  :   LL                     \ ~#&&\
  | //  \\ |    [==============================]                    /~~~#&&)
  |// __ \\|     \##\/##\/##\/##\//,.\\#\/##\//                     )~(~~#&\
  |/ /  \ \;     +)\##/\##/\##/\#\\`'//\##/\#(                     |~~/\~~#&)
  | / __ \|      [============================]                    |~(  \#&(
  |/ //\\ |      ############XXXXXXXXXXXXXX####;                   |~~)  )#&)
   |//  \\|      ##=======##\xxxxxxxxxxxxxx##==#.                   \(  (#&/
   |/ __ \|      ##=======##\\XXXXXXXXXXXXX/##==#                    \) (#(
   | /  \ |      `##=====##'\\\xxxxxxxxxxx//##==#.                   (   )#)
   |/ __ \|       ##=====##;'\\\XXXXXXXXX///;##==#.                     (#/
    |//\\ |       `##====##- '\\\xxxxxxx///;  ##==#                      /
    |,  \\|        ##====## -+'\\\XXXXX///;  ; ##=#.                    /
     |   ( )       `##===## ;  '\\\xxx///; -+-  ;#=#.
     | _/&\         `##==##+-  ;'\\\X///;  ;  -+- #=#.
     |/&&#~\         ##==##  -+- '\\_//; -+-  ;  -+-##.
     (&&#~~~\        ##==##  ;  -+'\_/;  ;  -+-  ;   HJ
     /&#~~)~(        `#### -+-  ;  'H.X-+-  ;  -+-  ;HJ.
    (&#~~/\~~|        ###  ;  -+-  HJLH;  -+-  ;  -+- HJ
     )&#/  )~|        ## -+-  ;   +HJ LH  ;  -+-  ;   HJ
    (&#(  (~~|        #  ;  -+-  ;HJ' `LH+   ;  -+-  ;HJ
     \&#)  )/         LH+-  ;  -+-HH   `LH -+-  ;  -+-HJ
      )#) (/          `LH -+-  ;  HH    `LH;  -+-  ;  HJ
     (#(   )           HH ;  -+-  HJ     `LH: ;  -+-  HJ
      \#)             ,HH+-  ;  -+HJ      LH:+-  ;  -+HJ
       \        _____ LH;  -+-  ;HJ' _    `LH__-+-__;_HJ___/\
        \       \ __ \LH___;  -+_HJ_/ /   (_______________/ /
                 \\W\______ \_;/____ /    .M=+++X)=++==++/ /
                  ))WWWWWWW\____/WW//     LH ;  (+==+==+/ /
                  ))WWWWWWWWWWWWWW//     LH-+-  ;)==+=+/ /
                  \\+==++WWWWWWW+((      LH;  -+(+==++/ /
                   ))==++==+++==+//     LH+-.W; .)==+/ /
                  //==+++==++==+//      LH  W'  (+==/ /
                 ((+==+++==++==//       LH  W  -+)==) )
                 ((==+++==++==+))       LH-+W  ;(+=(_/
                 ((==+++==++==+))       LH; W-+- )==(
                 ((==++==+++==+))       LH  W;  (+==/
                  \\=++==++==+//        LH -+W.  )=(
                   ))+==++==+//         LH ; 'W+(+=/
                  ((++==+==+((          LH+-  ;  )=)
                  ((+==++==+//           LH -+- (+/
                  ((+==+==++))           LH ;  -//
                 .//==+==++//            LH+   (]].
                 [[#######]]             [[#####]]
                .[[######]]              [[#####]]
                [[-#-#-#]]               [[-#-#-]]
                ;()()()();               :()()();
               ;, , , , ;                ;. . . :
              ;!,~,~,~,!;               ;~'~'~'~;
             :!~,~,~,~,!;              ;~'~'~-`:
            :!,~,~,~,~,!;            .;.'~'~'`-`:
           ;!' -' -' -!;            ;~~~;'~'~-`-`:
           ;' -' -' - !;             ;.~'~'~'~`-`-`..
           ;'()'()'() ;;            ;~~~~~~~~'~`-`-`-`.
           ;;^::/\:/\ ;;            :;;;;;;;;;~'~' -`-:`.
           :  ;; ;; ;;;                    ````.;;;~~~~`(`.
          (  ;  ;   ;;                           ```.;;;~`;`.
           `(  (   ;;                                 ```' ` )
             `  ` (                                         '
                   `

EOF
}

function __msg_info() {
  echo "INFO:" $1
}

function __msg_error() {
  echo "ERROR:" $1
}
#------------------------------END OF FUNCTIONS------------------------

__msg_info "Launching GET_TO_DA_CHOPPA.sh..."
validateDateArgumentGiven $1
commit_message=$1

if mvn clean verify | grep -q "BUILD SUCCESS"; then
  __msg_info "All test running green!!"
  __msg_info "GETTING TO DA CHOPPA!!!!!!!!!"
  printArnold
  afplay dotfiles/resources/get_to_da_choppa.mp3
  git commit -m commit_message

#  In case you are pushing towards a real repo, uncomment these lines:
#  current_branch_name=$(git rev-parse --abbrev-ref HEAD)
#  git push --set-upstream origin $current_branch_name
else
  __msg_error "COULD NOT GET TO DA CHOPPA!!!!!!"
  printPredator
  afplay dotfiles/resources/predator_sound.mp3
  __msg_error "Aborting: Failing tests"
fi

__msg_info "GET_TO_DA_CHOPPA.sh finished."
