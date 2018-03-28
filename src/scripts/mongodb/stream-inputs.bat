MODE con: cols=80 lines=25
TITLE MongoDB Server
python insert-simulator.py --src pendigits_full_with_head_noise5_train --timeout 5 --batch 5
EXIT
python insert-simulator.py --src pendigits_full_with_head_noise5_test --timeout 0 --batch 1