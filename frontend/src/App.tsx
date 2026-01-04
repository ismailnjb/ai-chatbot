import { useState } from "react";
import axios from "axios";

type Message = {
  role: "user" | "ai";
  content: string;
};

export default function App() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage: Message = { role: "user", content: input };
    setMessages((prev) => [...prev, userMessage]);
    setInput("");
    setLoading(true);

    try {
      const res = await axios.post(
        "http://localhost:8080/chat",
        { prompt: userMessage.content },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const data = await res.data;

      const aiMessage: Message = {
        role: "ai",
        content: data.response,
      };

      setMessages((prev) => [...prev, aiMessage]);
    } catch {
      setMessages((prev) => [
        ...prev,
        { role: "ai", content: "Error talking to AI server." },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 to-slate-800 text-white flex flex-col">
     <header className="sticky top-0 z-10 p-4 border-b border-slate-700 bg-slate-900 text-center text-xl font-semibold">
        AI Chatbot
      </header>

      <main className="flex-1 overflow-y-auto p-4 space-y-4 pb-24">
        {messages.map((msg, i) => (
          <div
            key={i}
            className={`max-w-xl px-4 py-2 rounded-xl whitespace-pre-wrap ${msg.role === "user"
              ? "ml-auto bg-blue-600"
              : "mr-auto bg-slate-700"
              }`}
          >
            {msg.content}
          </div>
        ))}

        {loading && (
          <div className="mr-auto bg-slate-700 px-4 py-2 rounded-xl max-w-xl">
            Typing...
          </div>
        )}
      </main>

     <footer className="sticky bottom-0 z-10 p-4 border-t border-slate-700 bg-slate-900 flex gap-2">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          placeholder="Ask something..."
          className="flex-1 rounded-lg px-4 py-2 bg-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          onClick={sendMessage}
          disabled={loading}
          className="bg-blue-600 hover:bg-blue-500 disabled:opacity-50 px-5 py-2 rounded-lg font-medium"
        >
          Send
        </button>
      </footer>
    </div>
  );
}
