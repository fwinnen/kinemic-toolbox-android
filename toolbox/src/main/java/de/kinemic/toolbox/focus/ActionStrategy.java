/*
 * Copyright (C) 2018 Kinemic GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kinemic.toolbox.focus;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Fabian on 06.02.18.
 */

public interface ActionStrategy {

    boolean up(View focus);
    boolean down(View focus);
    boolean left(View focus);
    boolean right(View focus);
    boolean select(View focus);
    boolean back(View focus);

    class DPadActions implements ActionStrategy {

        private void dispatchEvent(View target, int keyCode) {
            KeyEvent down = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
            KeyEvent up = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
            target.dispatchKeyEvent(down);
            target.dispatchKeyEvent(up);
        }

        @Override
        public boolean up(View focus) {
            dispatchEvent(focus, KeyEvent.KEYCODE_DPAD_UP);
            return true;
        }

        @Override
        public boolean down(View focus) {
            dispatchEvent(focus, KeyEvent.KEYCODE_DPAD_DOWN);
            return true;
        }

        @Override
        public boolean left(View focus) {
            dispatchEvent(focus, KeyEvent.KEYCODE_DPAD_LEFT);
            return true;
        }

        @Override
        public boolean right(View focus) {
            dispatchEvent(focus, KeyEvent.KEYCODE_DPAD_RIGHT);
            return true;
        }

        @Override
        public boolean select(View focus) {
            dispatchEvent(focus, KeyEvent.KEYCODE_DPAD_CENTER);
            return true;
        }

        @Override
        public boolean back(View focus) {
            dispatchEvent(focus, KeyEvent.KEYCODE_BACK);
            return true;
        }
    }

    class ListSelectionStrategy implements ActionStrategy {

        @Override
        public boolean up(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                list.setSelection(selection-1);
                return true;
            }
            return false;
        }

        @Override
        public boolean down(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                list.setSelection(selection+1);
                return true;
            }
            return false;
        }

        @Override
        public boolean left(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                list.setSelection(selection-1);
                return true;
            }
            return false;
        }

        @Override
        public boolean right(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                list.setSelection(selection+1);
                return true;
            }
            return false;
        }

        @Override
        public boolean select(View focus) {
            return false;
        }

        @Override
        public boolean back(View focus) {
            return false;
        }
    }

    class AnimatedListSelectionStrategy implements ActionStrategy {
        private static final String TAG = AnimatedListSelectionStrategy.class.getSimpleName();

        private void select(@NonNull ListView list, int selection) {
            if (selection >= list.getCount() || selection < 0) {
                Log.w(TAG, "invalid selection: " + selection);
                return;
            }

            int currentSelection = list.getSelectedItemPosition();


            list.setItemChecked(selection, true);
            int offset = list.getSelectedView().getHeight()  + list.getDividerHeight();

            /* set selection without scrolling */
            int currentTop = list.getSelectedView().getTop() - list.getListPaddingTop() + offset * (selection - currentSelection);
            list.setSelectionFromTop(selection, currentTop);

            /* scroll smoothly to center view */
            int centeredTop = (list.getHeight()-list.getPaddingTop()-list.getPaddingBottom()-list.getSelectedView().getHeight()) / 2;
            list.smoothScrollToPositionFromTop(selection, centeredTop);
        }

        @Override
        public boolean up(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                if (list.isFocused() && selection != ListView.INVALID_POSITION) {
                    select(list, selection - 1);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean down(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                if (list.isFocused() && selection != ListView.INVALID_POSITION) {
                    select(list, selection + 1);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean left(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                if (list.isFocused() && selection != ListView.INVALID_POSITION) {
                    select(list, selection - 1);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean right(View focus) {
            if (focus instanceof ListView) {
                ListView list = (ListView) focus;
                int selection = list.getSelectedItemPosition();
                if (list.isFocused() && selection != ListView.INVALID_POSITION) {
                    select(list, selection + 1);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean select(View focus) {
            return false;
        }

        @Override
        public boolean back(View focus) {
            return false;
        }
    }

    public class AndroidFocusStrategy implements ActionStrategy {

        @Override
        public boolean up(View focus) {
            return false;
        }

        @Override
        public boolean down(View focus) {
            return false;
        }

        @Override
        public boolean left(View focus) {
            return false;
        }

        @Override
        public boolean right(View focus) {
            return false;
        }

        @Override
        public boolean select(View focus) {
            return false;
        }

        @Override
        public boolean back(View focus) {
            return false;
        }
    }

}

